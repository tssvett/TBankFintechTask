package tssvett.dev.translator.utils.mapping;

import org.mapstruct.Mapper;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.request.TranslateRequestDto;
import tssvett.dev.translator.dto.response.TranslateResponseDto;

@Mapper(componentModel = "spring")
public interface TranslateDtoResponseMapper {


    TranslateResponseDto toTranslateResponseDto(TranslateRequestDto translateRequestDto);

    TranslateResponseDto toTranslateResponseDto(TranslateDto translateDto);

    TranslateDto toTranslateDto(TranslateRequestDto translateRequestDto);

}
