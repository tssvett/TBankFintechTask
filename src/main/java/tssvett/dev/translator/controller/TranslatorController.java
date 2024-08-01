package tssvett.dev.translator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tssvett.dev.translator.dto.request.TranslateRequestDto;
import tssvett.dev.translator.dto.response.TranslateResponseDto;
import tssvett.dev.translator.service.TranslatorService;
import tssvett.dev.translator.utils.mapping.TranslateDtoResponseMapper;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TranslatorController {
    private final TranslatorService translatorService;
    private final TranslateDtoResponseMapper translateDtoResponseMapper;

    @GetMapping("/translate")
    public TranslateResponseDto translate(@Valid @RequestBody TranslateRequestDto translateRequestDto) {
        return translateDtoResponseMapper.toTranslateResponseDto(translatorService.translate(translateRequestDto));
    }
}

