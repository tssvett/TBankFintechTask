package tssvett.dev.translator.integration.Impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
    public TranslateDto translate(TranslateRequestDto translateRequestDto) {
        YandexRequestDto yandexRequestDto = translateDtoResponseMapper.toYandexRequestDto(translateRequestDto);
        TranslateResponseDto response = callTranslationApi(yandexProperties.getUrl(), yandexRequestDto);
        TranslateDto translateDto = translateDtoResponseMapper.toTranslateDto(translateRequestDto);
        translateDto.setTranslatedStrings(response.getTranslatedStrings());

        return translateDto;
    }

    private TranslateResponseDto callTranslationApi(String url, YandexRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Api-Key " + yandexProperties.getApiKey());
        HttpEntity<YandexRequestDto> entity = new HttpEntity<>(request, headers);
        try {
            log.info("Sending request to Yandex API : {}", request);
            ResponseEntity<YandexResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, YandexResponseDto.class);
            return translateDtoResponseMapper.toTranslateResponseDto(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error calling Yandex API: {}", e.getMessage());
            throw new TooManyRequestsException(e.getMessage());
        }
    }
}
