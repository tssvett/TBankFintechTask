package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tssvett.dev.translator.validation.annotation.ValidLanguage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslateRequestDto {

    @NotBlank(message = "Text to translate cannot be empty")
    private String textToTranslate;

    @NotBlank(message = "Source language cannot be empty")
    @ValidLanguage
    private String sourceLanguage;

    @NotBlank(message = "Target language cannot be empty")
    @ValidLanguage
    private String targetLanguage;
}
