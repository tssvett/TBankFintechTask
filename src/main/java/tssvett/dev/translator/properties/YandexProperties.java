package tssvett.dev.translator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "integrations.yandex")
@Getter
@Setter
public class YandexProperties {

    private String apiKey;
    private String url;
}
