package subway.line.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.dao.LineDao;
import subway.domain.line.domain.Line;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private LineDao lineDao;

    @BeforeEach
    void init() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void Line_정보를_추가할_수_있다() {
        //given
        Line line = new Line("4호선", "노란색");

        //when
        Line insertLine = lineDao.insert(line);

        //then
        Assertions.assertThat(insertLine)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(line);
    }

    @Test
    void Line_정보를_추가_실패_테스트_존재하는_이름으로_추가() {
        //given
        Line line = new Line("3호선", "주황색");

        //when
        assertThatThrownBy(() -> lineDao.insert(line)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보를_수정할_수_있다() {
        //given
        Line line = new Line("4호선", "노란색");
        Line insertLine = lineDao.insert(line);

        //when
        Line updateLine = new Line(insertLine.getId(), "5호선", "주황색");
        lineDao.update(updateLine);

        //then
        Line findLine = lineDao.findById(updateLine.getId());

        Assertions.assertThat(findLine).isEqualTo(findLine);
    }

    @Test
    void Line_정보_수정_실패_테스트_존재하는_이름으로_변경() {
        //given
        Line line = new Line("4호선", "노란색");
        Line insertLine = lineDao.insert(line);

        //then
        Line updateLine = new Line(insertLine.getId(), "3호선", "주황색");
        assertThatThrownBy(() -> lineDao.update(updateLine)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보_삭제_테스트() {
        //given
        Line line = new Line("4호선", "노란색");
        Line insertLine = lineDao.insert(line);

        //then
        assertAll(
                () -> assertDoesNotThrow(() -> lineDao.deleteById(insertLine.getId())),
                () -> assertThatThrownBy(() -> lineDao.findById(insertLine.getId())).isInstanceOf(EmptyResultDataAccessException.class)
        );
    }

    @Test
    void Line_단일_검색_테스트() {
        //then
        Line line = lineDao.findById(1L);
        Assertions.assertThat(line).isEqualTo(new Line(1L, "2호선", "초록색"));
    }

    @Test
    void Line_이름_검색_테스트() {
        //then
        Optional<Line> line = lineDao.findByName("2호선");
        Assertions.assertThat(line).isPresent();
    }

    @Test
    void Line_이름_검색_실패_테스트() {
        //then
        Optional<Line> line = lineDao.findByName("5호선");
        Assertions.assertThat(line).isEmpty();
    }

    @Test
    void Line_전체_검색_테스트() {
        //then
        List<Line> lines = lineDao.findAll();
        Assertions.assertThat(lines).hasSize(2);
    }
}
