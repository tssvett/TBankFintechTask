package tssvett.dev.translator.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslateResponseDto;
import tssvett.dev.translator.dto.yandex.YandexRequestDto;
import tssvett.dev.translator.dto.yandex.YandexResponseDto;
import tssvett.dev.translator.properties.YandexProperties;
import tssvett.dev.translator.service.TranslatorService;
import tssvett.dev.translator.utils.mapping.TranslateDtoResponseMapper;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class YandexTranslatorService implements TranslatorService {

    private final RestTemplate restTemplate;
    private final YandexProperties yandexProperties;
    private final TranslateDtoResponseMapper translateDtoResponseMapper;

    @Override
    public TranslateDto translate(TranslateRequestDto translateRequestDto) {
        YandexRequestDto yandexRequestDto = translateDtoResponseMapper.toYandexRequestDto(translateRequestDto);
        TranslateResponseDto response = callTranslationApi(yandexProperties.getUrl(), yandexRequestDto);
        log.info("Translated text: {}", response.getTranslatedTranslatedString());

        return translateDtoResponseMapper.toTranslateDto(response);
    }

    private TranslateResponseDto callTranslationApi(String url, YandexRequestDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Api-Key " + yandexProperties.getApiKey());
        HttpEntity<YandexRequestDto> entity = new HttpEntity<>(request, headers);
        ResponseEntity<YandexResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, YandexResponseDto.class);

        return translateDtoResponseMapper.toTranslateResponseDto(response.getBody());
    }
}
