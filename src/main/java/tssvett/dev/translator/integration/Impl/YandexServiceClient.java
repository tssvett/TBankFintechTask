package tssvett.dev.translator.integration.Impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslateResponseDto;
import tssvett.dev.translator.dto.yandex.YandexRequestDto;
import tssvett.dev.translator.dto.yandex.YandexResponseDto;
import tssvett.dev.translator.handler.exception.TooManyRequestsException;
import tssvett.dev.translator.integration.TranslateServiceClient;
import tssvett.dev.translator.properties.YandexProperties;
import tssvett.dev.translator.utils.mapping.TranslateDtoResponseMapper;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class YandexServiceClient implements TranslateServiceClient {

    private final RestTemplate restTemplate;
    private final YandexProperties yandexProperties;
    private final TranslateDtoResponseMapper translateDtoResponseMapper;

    @Override
    public TranslateDto translate(TranslateRequestDto translateRequestDto, String ipAddress) {
        YandexRequestDto yandexRequestDto = translateDtoResponseMapper.toYandexRequestDto(translateRequestDto);
        TranslateResponseDto response = callTranslationApi(yandexProperties.getUrl(), yandexRequestDto);
        TranslateDto translateDto = translateDtoResponseMapper.toTranslateDto(translateRequestDto);
        translateDto.setTranslatedStrings(response.getTranslatedStrings());
        translateDto.setIpAddress(ipAddress);
        return translateDto;
    }

    private TranslateResponseDto callTranslationApi(String url, YandexRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Api-Key " + yandexProperties.getApiKey());
        HttpEntity<YandexRequestDto> entity = new HttpEntity<>(request, headers);

        int retryCount = 0;
        int maxRetries = 50;
        long waitTime = yandexProperties.getRetryDelayInSeconds();

        while (true) {
            try {
                log.info("Sending request to Yandex API : {}", request);
                ResponseEntity<YandexResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, YandexResponseDto.class);
                return translateDtoResponseMapper.toTranslateResponseDto(response.getBody());
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == 429) { // Код 429 - слишком много запросов
                    if (retryCount < maxRetries) {
                        log.warn("Too many requests to Yandex API. Waiting for {} ms before retrying...", waitTime);
                        try {
                            Thread.sleep(waitTime);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        retryCount++;
                        waitTime *= 2; // Увеличиваем время ожидания (экспоненциальная задержка)
                    } else {
                        log.error("Max retries reached. Unable to call Yandex API.");
                        throw new TooManyRequestsException("Exceeded maximum retries for Yandex API: " + e.getMessage());
                    }
                } else {
                    log.error("Error calling Yandex API: {}", e.getMessage());
                    throw new TooManyRequestsException(e.getMessage());
                }
            }
        }
    }
}
