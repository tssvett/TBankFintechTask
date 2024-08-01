package tssvett.dev.translator.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.request.TranslateRequestDto;
import tssvett.dev.translator.service.TranslatorService;

@Service
@Slf4j
public class TranslatorServiceMock implements TranslatorService {
    @Override
    public TranslateDto translate(TranslateRequestDto translateRequestDto) {
        log.info("Translate {} from {} to {}", translateRequestDto.getTextToTranslate(),
                translateRequestDto.getSourceLanguage(), translateRequestDto.getTargetLanguage());

        return TranslateDto.builder()
                .textToTranslate(translateRequestDto.getTextToTranslate())
                .sourceLanguage(translateRequestDto.getSourceLanguage())
                .targetLanguage(translateRequestDto.getTargetLanguage())
                .translatedText("MOCK Translated text")
                .build();
    }
}
