package tssvett.dev.translator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslateResponseDto {

    @NotNull(message = "List of translated string cannot be null")
    private List<TranslatedString> translatedTranslatedString;
}
