package tssvett.dev.translator.utils.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tssvett.dev.translator.dto.TranslateDto;
import tssvett.dev.translator.dto.TranslateRequestDto;
import tssvett.dev.translator.dto.TranslateResponseDto;
import tssvett.dev.translator.dto.yandex.YandexRequestDto;
import tssvett.dev.translator.dto.yandex.YandexResponseDto;

@Mapper(componentModel = "spring")
public interface TranslateDtoResponseMapper {

    TranslateResponseDto toTranslateResponseDto(TranslateRequestDto translateRequestDto);

    TranslateResponseDto toTranslateResponseDto(TranslateDto translateDto);

    TranslateRequestDto toTranslateRequestDto(TranslateDto translateDto);

    TranslateDto toTranslateDto(TranslateRequestDto translateRequestDto);

    @Mapping(source = "translatedStrings", target = "translatedStrings")
    TranslateDto toTranslateDto(TranslateResponseDto translateResponseDto);

    @Mapping(source = "translations", target = "translatedStrings")
    TranslateResponseDto toTranslateResponseDto(YandexResponseDto yandexResponseDto);

    @Mapping(source = "targetLanguage", target = "targetLanguageCode")
    @Mapping(source = "sourceLanguage", target = "sourceLanguageCode")
    @Mapping(source = "textToTranslate", target = "texts")
    YandexRequestDto toYandexRequestDto(TranslateRequestDto translateRequestDto);
}
