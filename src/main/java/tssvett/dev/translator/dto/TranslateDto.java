package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TranslateDto {

    @NotBlank(message = "Text to translate cannot be empty")
    String textToTranslate;

    @NotBlank(message = "Source language cannot be empty")
    String sourceLanguage;

    @NotBlank(message = "Target language cannot be empty")
    String targetLanguage;

    @NotNull(message = "List of translated string cannot be null")
    List<TranslatedString> translatedTranslatedString;
}
