package tssvett.dev.translator.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslatedString;
import tssvett.dev.translator.entity.Translation;
import tssvett.dev.translator.handler.exception.TooManyRequestsException;
import tssvett.dev.translator.integration.Impl.YandexServiceClient;
import tssvett.dev.translator.repository.TranslationRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
    private final TranslationRepository translationRepository;
    private final List<Instant> requestTimes = new CopyOnWriteArrayList<>();

    public TranslateDto translateInParallel(TranslateRequestDto translateRequestDto) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> textToTranslate = Arrays.asList(translateRequestDto.getTextToTranslate().split(" "));
        String ipAddress = getIpAddress();

        List<CompletableFuture<TranslateDto>> futures = textToTranslate.stream()
                .map(word -> CompletableFuture.supplyAsync(() -> translateWord(word, translateRequestDto, ipAddress), executorService))
                .collect(Collectors.toList());

        List<TranslateDto> translatedWords = collectTranslations(futures);

        // Пакетное сохранение переводов в базу данных
        List<Translation> translations = buildTranslations(translatedWords, ipAddress);
        translationRepository.saveTranslations(translations);

        executorService.shutdown();

        return buildFinalTranslation(translatedWords, translateRequestDto);
    }

    private String getIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("Client IP: " + request.getRemoteAddr());

            return request.getRemoteAddr();
        } else {
            log.warn("No request attributes found. Unable to retrieve client IP.");
        }

        return null;
    }

    private TranslateDto translateWord(String word, TranslateRequestDto translateRequestDto, String ipAddress) {
        trackRequestTime();

        TranslateRequestDto request = TranslateRequestDto.builder()
                .textToTranslate(word)
                .sourceLanguage(translateRequestDto.getSourceLanguage())
                .targetLanguage(translateRequestDto.getTargetLanguage())
                .build();

        return yandexServiceClient.translate(request, ipAddress);
    }

    private List<TranslateDto> collectTranslations(List<CompletableFuture<TranslateDto>> futures) {
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private List<Translation> buildTranslations(List<TranslateDto> translatedWords, String ipAddress) {
        List<Translation> translations = new ArrayList<>();

        for (TranslateDto translatedDto : translatedWords) {
            Translation translation = Translation.builder()
                    .textToTranslate(translatedDto.getTextToTranslate())
                    .translatedText(translatedDto.getTranslatedStrings().get(0).getText())
                    .ipAddress(ipAddress)
                    .build();
            translations.add(translation);
        }

        return translations;
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
