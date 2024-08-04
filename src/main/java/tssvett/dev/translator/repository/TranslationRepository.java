package tssvett.dev.translator.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tssvett.dev.translator.entity.Translation;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TranslationRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveTranslations(List<Translation> translations) {
        String sql = "INSERT INTO translation (id, text, translated_text, ip_address) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Translation translation = translations.get(i);
                ps.setObject(1, UUID.randomUUID());
                ps.setString(2, translation.getTextToTranslate());
                ps.setString(3, translation.getTranslatedText());
                ps.setString(4, translation.getIpAddress());
            }

            @Override
            public int getBatchSize() {
                return translations.size();
            }
        });
    }
}