package tssvett.dev.translator.dto.yandex;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tssvett.dev.translator.dto.TranslatedString;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YandexRequestDto {

    @NotBlank(message = "Language code cannot be empty")
    private String targetLanguageCode;

    @NotBlank(message = "Language code cannot be empty")
    private String sourceLanguageCode;

    @NotBlank(message = "Text to translate cannot be empty")
    private String texts;
}
