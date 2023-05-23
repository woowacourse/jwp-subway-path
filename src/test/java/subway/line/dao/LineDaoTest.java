package subway.line.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.entity.LineEntity;

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
        LineEntity lineEntity = new LineEntity("4호선", "노란색");

        //when
        LineEntity insertLineEntity = lineDao.insert(lineEntity);

        //then
        Assertions.assertThat(insertLineEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(lineEntity);
    }

    @Test
    void Line_정보를_추가_실패_테스트_존재하는_이름으로_추가() {
        //given
        LineEntity lineEntity = new LineEntity("3호선", "주황색");

        //when
        assertThatThrownBy(() -> lineDao.insert(lineEntity)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보를_수정할_수_있다() {
        //given
        LineEntity lineEntity = new LineEntity("4호선", "노란색");
        LineEntity insertLineEntity = lineDao.insert(lineEntity);

        //when
        LineEntity updateLineEntity = new LineEntity(insertLineEntity.getId(), "5호선", "주황색");
        lineDao.update(updateLineEntity);

        //then
        Optional<LineEntity> line = lineDao.findById(updateLineEntity.getId());

        Assertions.assertThat(line.get()).isEqualTo(updateLineEntity);
    }

    @Test
    void Line_정보_수정_실패_테스트_존재하는_이름으로_변경() {
        //given
        LineEntity lineEntity = new LineEntity("4호선", "노란색");
        LineEntity insertLineEntity = lineDao.insert(lineEntity);

        //then
        LineEntity updateLineEntity = new LineEntity(insertLineEntity.getId(), "3호선", "주황색");
        assertThatThrownBy(() -> lineDao.update(updateLineEntity)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보_삭제_테스트() {
        //given
        LineEntity lineEntity = new LineEntity("4호선", "노란색");
        LineEntity insertLineEntity = lineDao.insert(lineEntity);

        //then
        assertAll(
                () -> assertDoesNotThrow(() -> lineDao.deleteById(insertLineEntity.getId())),
                () -> lineDao.findById(insertLineEntity.getId()).isEmpty()
        );
    }

    @Test
    void Line_단일_검색_테스트() {
        //then
        Optional<LineEntity> line = lineDao.findById(1L);
        Assertions.assertThat(line.get()).isEqualTo(new LineEntity(1L, "2호선", "초록색"));
    }

    @Test
    void Line_이름_검색_테스트() {
        //then
        Optional<LineEntity> line = lineDao.findByName("2호선");
        Assertions.assertThat(line).isPresent();
    }

    @Test
    void Line_이름_검색_실패_테스트() {
        //then
        Optional<LineEntity> line = lineDao.findByName("5호선");
        Assertions.assertThat(line).isEmpty();
    }

    @Test
    void Line_전체_검색_테스트() {
        //then
        Optional<List<LineEntity>> lines = lineDao.findAll();
        Assertions.assertThat(lines.get()).hasSize(2);
    }
}
