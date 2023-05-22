package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_schema.sql")
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void 호선_정보를_삽입한다() {
        // given
        final Line lineNumber2 = new Line("2호선", "green");
        // when
        final Line insertedLine = lineDao.insert(lineNumber2);
        // then
        assertSoftly(softly -> {
            softly.assertThat(insertedLine.getName()).isEqualTo("2호선");
            softly.assertThat(insertedLine.getColor()).isEqualTo("green");
        });
    }

    @Test
    void 전체_호선_정보를_조회한다() {
        // given
        final Line lineNumber2 = new Line("2호선", "green");
        final Line lineNumber8 = new Line("8호선", "pink");
        lineDao.insert(lineNumber2);
        lineDao.insert(lineNumber8);
        // when
        final List<Line> allLines = lineDao.findAll();
        // then
        assertSoftly(softly -> {
            softly.assertThat(allLines).hasSize(2);
            final Line firstLine = allLines.get(0);
            softly.assertThat(firstLine.getName()).isEqualTo("2호선");
            softly.assertThat(firstLine.getColor()).isEqualTo("green");
            final Line secondLine = allLines.get(1);
            softly.assertThat(secondLine.getName()).isEqualTo("8호선");
            softly.assertThat(secondLine.getColor()).isEqualTo("pink");
        });
    }

    @Test
    void id로_호선_정보를_조회한다() {
        // given
        final Line lineNumber2 = new Line("2호선", "green");
        final Long id = lineDao.insert(lineNumber2).getId();
        // when
        final Optional<Line> actualLine = lineDao.findById(id);
        // then
        assertSoftly(softly -> {
            softly.assertThat(actualLine.get().getName()).isEqualTo("2호선");
            softly.assertThat(actualLine.get().getColor()).isEqualTo("green");
        });
    }

    @Test
    void 호선_정보를_수정한다() {
        // given
        final Line lineNumber2 = new Line("2호선", "green");
        final Long id = lineDao.insert(lineNumber2).getId();
        // when
        lineDao.update(new Line(id, "8호선", "pink"));
        // then
        assertSoftly(softly -> {
            final Optional<Line> line = lineDao.findById(id);
            softly.assertThat(line.get().getName()).isEqualTo("8호선");
            softly.assertThat(line.get().getColor()).isEqualTo("pink");
        });
    }

    @Test
    void id로_호선_정보를_삭제한다() {
        // given
        final Line lineNumber2 = new Line("2호선", "green");
        final Long id = lineDao.insert(lineNumber2).getId();
        // when
        lineDao.deleteById(id);
        // then
        final List<Line> allLines = lineDao.findAll();
        assertThat(allLines).hasSize(0);
    }
}
