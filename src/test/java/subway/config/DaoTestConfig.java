package subway.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public abstract class DaoTestConfig {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE STATIONS");
        jdbcTemplate.update("TRUNCATE TABLE SECTIONS");
        jdbcTemplate.update("TRUNCATE TABLE LINES");
        jdbcTemplate.update("TRUNCATE TABLE STATIONS_LINES");
    }
}
