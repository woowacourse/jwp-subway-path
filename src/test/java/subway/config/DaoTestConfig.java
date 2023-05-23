package subway.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@JdbcTest
@DisplayNameGeneration(ReplaceUnderscores.class)
public abstract class DaoTestConfig {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE station");
        jdbcTemplate.update("TRUNCATE TABLE section");
        jdbcTemplate.update("TRUNCATE TABLE line");
    }
}
