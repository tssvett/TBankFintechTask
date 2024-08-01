package tssvett.dev.translator.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslateResponseDto {
    @NotBlank(message = "Translated text cannot be empty")
    String translatedText;
}
