package subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@Sql({"classpath:schema-test.sql"})
class LineDaoTest {

    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 호선을_생성한다() {
        // given
        Line line = Line.of("2호선", "초록");

        // when
        final Long id = lineDao.insert(line);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 모든_호선을_조회한다() {
        // given
        Line line1 = Line.of("1호선", "파랑");
        Line line2 = Line.of("2호선", "초록");
        Line line3 = Line.of("3호선", "노랑");
        lineDao.insert(line1);
        lineDao.insert(line2);
        lineDao.insert(line3);

        // when
        final List<Line> results = lineDao.findAll();

        // then
        assertAll(
                () -> assertThat(results).extracting(Line::getName)
                        .contains("1호선", "2호선", "3호선"),
                () -> assertThat(results).extracting(Line::getColor)
                        .contains("파랑", "초록", "노랑")
        );
    }

    @Test
    void 호선을_ID로_조회한다() {
        // given
        Line line = Line.of("2호선", "초록");
        final Long id = lineDao.insert(line);

        // when
        final Line result = lineDao.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록")
        );
    }

    @Test
    void 호선을_수정한다() {
        // given
        Line line = Line.of("2호선", "초록");
        final Long id = lineDao.insert(line);

        // when
        lineDao.updateById(id, Line.of("2호선", "검정"));
        final Line result = lineDao.findById(id);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("검정")
        );
    }

    @Test
    void 호선을_삭제한다() {
        // given
        Line line = Line.of("2호선", "초록");
        final Long id = lineDao.insert(line);
        lineDao.deleteById(id);

        // when, then
        assertThrows(
                DataAccessException.class, () -> lineDao.findById(id)
        );
    }

}
