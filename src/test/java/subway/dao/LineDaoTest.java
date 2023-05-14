package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql("classpath:schema.sql")
class LineDaoTest {

    private final LineDao lineDao;

    private LineDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.lineDao = new LineDao(jdbcTemplate);
    }


}
