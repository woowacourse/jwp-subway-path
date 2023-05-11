package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void Line을_저장한다() {
        // given
        LineEntity line = new LineEntity("2호선");

        // when
        LineEntity saveLine = lineDao.insert(line);

        // then
        assertThat(saveLine.getId()).isPositive();
    }
}
