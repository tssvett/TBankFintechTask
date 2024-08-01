package tssvett.dev.translator.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslatedString;
import tssvett.dev.translator.service.TranslatorService;

import java.util.List;

@Slf4j
@Service
public class TranslatorServiceMock implements TranslatorService {
    @Override
    public TranslateDto translate(TranslateRequestDto translateRequestDto) {
        log.info("Translate {} from {} to {}", translateRequestDto.getTextToTranslate(),
                translateRequestDto.getSourceLanguage(), translateRequestDto.getTargetLanguage());

        return TranslateDto.builder()
                .textToTranslate(translateRequestDto.getTextToTranslate())
                .sourceLanguage(translateRequestDto.getSourceLanguage())
                .targetLanguage(translateRequestDto.getTargetLanguage())
                .translatedTranslatedString(List.of(TranslatedString.builder().text("MOCK Translated text").build()))
                .build();
    }
}
