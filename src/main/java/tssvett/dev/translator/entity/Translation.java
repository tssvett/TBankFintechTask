package tssvett.dev.translator.entity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Translation {

    private UUID id;
    private String ipAddress;
    private String translatedText;
    private String textToTranslate;
}
