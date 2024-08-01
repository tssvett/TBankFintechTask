package tssvett.dev.translator.service;

import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.request.TranslateRequestDto;

public interface TranslatorService {

    TranslateDto translate(TranslateRequestDto translateRequestDto);
}
