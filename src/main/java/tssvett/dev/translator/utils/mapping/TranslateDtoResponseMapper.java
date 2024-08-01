package tssvett.dev.translator.utils.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslateResponseDto;

@Mapper(componentModel = "spring")
public interface TranslateDtoResponseMapper {


    TranslateResponseDto translate(TranslateRequestDto translateRequestDto);

    TranslateResponseDto translate(TranslateDto translateDto);
}
