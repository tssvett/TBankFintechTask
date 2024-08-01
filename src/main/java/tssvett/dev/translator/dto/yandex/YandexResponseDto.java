package tssvett.dev.translator.dto.yandex;

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
public class YandexResponseDto {

    private List<TranslatedString> translations;
}
