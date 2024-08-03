package tssvett.dev.translator.dto.yandex;

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
public class YandexRequestDto {

    @NotBlank(message = "Language code cannot be empty")
    @ValidLanguage
    private String targetLanguageCode;

    @NotBlank(message = "Language code cannot be empty")
    @ValidLanguage
    private String sourceLanguageCode;

    @NotBlank(message = "Text to translate cannot be empty")
    private String texts;
}
