package tssvett.dev.translator.integration;

import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;

public interface TranslateServiceClient {

    TranslateDto translate(TranslateRequestDto translateRequestDto);
}
