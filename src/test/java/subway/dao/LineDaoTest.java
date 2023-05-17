package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.fixture.LineFixture.Line1;
import subway.fixture.LineFixture.Line2;

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
        final Line line = Line1.line;

        // when
        final Long id = lineDao.insert(line);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 모든_호선을_조회한다() {
        // given
        final Line line1 = Line1.line;
        final Line line2 = Line2.line;
        lineDao.insert(line1);
        lineDao.insert(line2);

        // when
        final List<Line> results = lineDao.findAll();

        // then
        assertAll(
                () -> assertThat(results).extracting(Line::getLineName).extracting(LineName::name)
                        .contains("1호선", "2호선"),
                () -> assertThat(results).extracting(Line::getLineColor).extracting(LineColor::color)
                        .contains("파랑", "초록")
        );
    }

    @Test
    void 호선을_ID로_조회한다() {
        // given
        final Long id = lineDao.insert(Line1.line);

        // when
        final Line result = lineDao.findById(id).get();

        // then
        assertAll(
                () -> assertThat(result.getLineName().name()).isEqualTo("1호선"),
                () -> assertThat(result.getLineColor().color()).isEqualTo("파랑")
        );
    }

    @Test
    void 호선을_수정한다() {
        // given
        final Long id = lineDao.insert(Line1.line);

        // when
        lineDao.updateById(new Line(
                id,
                new LineName("1호선"),
                new LineColor("검정"))
        );

        final Line result = lineDao.findById(id).get();

        // then
        assertAll(
                () -> assertThat(result.getLineName().name()).isEqualTo("1호선"),
                () -> assertThat(result.getLineColor().color()).isEqualTo("검정")
        );
    }

    @Test
    void 호선을_삭제한다() {
        // given
        final Long id = lineDao.insert(Line1.line);
        lineDao.deleteById(id);

        // when, then
        assertThatThrownBy(() -> assertThat(lineDao.findById(id).get()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 등록된_호선을_이름으로_조회한다() {
        // given
        lineDao.insert(Line1.line);

        // when
        final Line result = lineDao.findByName(new LineName("1호선")).get();

        // then
        assertThat(result.getLineName().name()).isEqualTo("1호선");
    }
}
