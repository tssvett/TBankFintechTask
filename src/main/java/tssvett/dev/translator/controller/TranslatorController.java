package tssvett.dev.translator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslateResponseDto;
import tssvett.dev.translator.service.TranslateService;
import tssvett.dev.translator.utils.mapping.TranslateDtoResponseMapper;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TranslatorController {

    private final TranslateService translateService;
    private final TranslateDtoResponseMapper translateDtoResponseMapper;

    @PostMapping("/translate")
    public TranslateResponseDto translate(@Valid @RequestBody TranslateRequestDto translateRequestDto) {
        TranslateDto translateDto = translateService.translateInParallel(translateRequestDto);
        log.info("Translated text: {}", translateDto);
        return translateDtoResponseMapper.toTranslateResponseDto(translateDto);
    }
}
