package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslateDto {
    @NotBlank(message = "Text to translate cannot be empty")
    String textToTranslate;
    @NotBlank(message = "Source language cannot be empty")
    String sourceLanguage;
    @NotBlank(message = "Target language cannot be empty")
    String targetLanguage;
    @NotBlank(message = "Translated text cannot be empty")
    String translatedText;
}
