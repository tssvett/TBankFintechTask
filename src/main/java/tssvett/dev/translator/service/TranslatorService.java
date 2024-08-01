package tssvett.dev.translator.service;

import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;

public interface TranslatorService {

    TranslateDto translate(TranslateRequestDto translateRequestDto);
}
