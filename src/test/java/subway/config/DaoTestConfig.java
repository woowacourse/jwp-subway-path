package subway.config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@JdbcTest
@Sql("/truncate.sql")
@DisplayNameGeneration(ReplaceUnderscores.class)
public abstract class DaoTestConfig {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
