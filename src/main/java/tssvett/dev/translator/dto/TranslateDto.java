package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tssvett.dev.translator.validation.annotation.ValidLanguage;

import java.util.List;

@Data
@Builder
public class TranslateDto {

    @NotBlank(message = "Text to translate cannot be empty")
    String textToTranslate;

    @NotBlank(message = "Source language cannot be empty")
    @ValidLanguage
    String sourceLanguage;

    @NotBlank(message = "Target language cannot be empty")
    @ValidLanguage
    String targetLanguage;

    String ipAddress;

    @NotNull(message = "List of translated string cannot be null")
    List<TranslatedString> translatedStrings;
}
