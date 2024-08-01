package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslateRequestDto {

    @NotBlank(message = "Text to translate cannot be empty")
    private String textToTranslate;

    @NotBlank(message = "Source language cannot be empty")
    private String sourceLanguage;

    @NotBlank(message = "Target language cannot be empty")
    private String targetLanguage;
}
