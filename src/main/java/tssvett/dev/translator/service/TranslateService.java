package tssvett.dev.translator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslatedString;
import tssvett.dev.translator.handler.exception.TooManyRequestsException;
import tssvett.dev.translator.integration.Impl.YandexServiceClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranslateService {

    private static final Duration RATE_LIMIT_WINDOW = Duration.ofSeconds(1);

    private final YandexServiceClient yandexServiceClient;
    private final List<Instant> requestTimes = new CopyOnWriteArrayList<>();

    public TranslateDto translateInParallel(TranslateRequestDto translateRequestDto) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> textToTranslate = Arrays.asList(translateRequestDto.getTextToTranslate().split(" "));

        List<CompletableFuture<TranslateDto>> futures = textToTranslate.stream()
                .map(word -> CompletableFuture.supplyAsync(() -> translateWord(word, translateRequestDto), executorService))
                .collect(Collectors.toList());

        List<TranslateDto> translatedWords = collectTranslations(futures);

        executorService.shutdown();

        return buildFinalTranslation(translatedWords, translateRequestDto);
    }

    private TranslateDto translateWord(String word, TranslateRequestDto translateRequestDto) {
        trackRequestTime();
        TranslateRequestDto request = new TranslateRequestDto(word, translateRequestDto.getSourceLanguage(), translateRequestDto.getTargetLanguage());

        return translateWithRetry(request);
    }

    private List<TranslateDto> collectTranslations(List<CompletableFuture<TranslateDto>> futures) {
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private TranslateDto buildFinalTranslation(List<TranslateDto> translatedWords, TranslateRequestDto translateRequestDto) {
        String combinedTranslation = translatedWords.stream()
                .flatMap(translatedDto -> translatedDto.getTranslatedStrings().stream()
                        .map(TranslatedString::getText))
                .collect(Collectors.joining(" "));

        return TranslateDto.builder()
                .translatedStrings(List.of(new TranslatedString(combinedTranslation)))
                .textToTranslate(translateRequestDto.getTextToTranslate())
                .targetLanguage(translateRequestDto.getTargetLanguage())
                .sourceLanguage(translateRequestDto.getSourceLanguage())
                .build();
    }

    private TranslateDto translateWithRetry(TranslateRequestDto translateRequestDto) {
        try {
            return yandexServiceClient.translate(translateRequestDto);
        } catch (TooManyRequestsException e) {
            log.warn("Too many requests to Yandex API, waiting before retrying.");
            sleep(yandexServiceClient.getYandexProperties().getRetryDelayInSeconds() * 1000);
            // умножаем на 1000 тк в миллисекундах параметр

            return yandexServiceClient.translate(translateRequestDto);
        }
    }

    private void trackRequestTime() {
        Instant now = Instant.now();
        requestTimes.add(now);
        requestTimes.removeIf(instant -> now.minus(RATE_LIMIT_WINDOW).isAfter(instant));
        if (requestTimes.size() > yandexServiceClient.getYandexProperties().getMaxRequestsPerSecond()) {
            long delayMillis = Duration.between(requestTimes.get(0), now).toMillis();
            sleep(delayMillis);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
