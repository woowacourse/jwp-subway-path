package subway.lineDetail.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.entity.LineDetailEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineDetailDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private LineDetailDao lineDetailDao;

    @BeforeEach
    void init() {
        lineDetailDao = new LineDetailDao(jdbcTemplate, dataSource);
    }

    @Test
    void Line_정보를_추가할_수_있다() {
        //given
        LineDetailEntity lineDetailEntity = new LineDetailEntity("4호선", "노란색");

        //when
        LineDetailEntity insertLineDetailEntity = lineDetailDao.insert(lineDetailEntity);

        //then
        Assertions.assertThat(insertLineDetailEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(lineDetailEntity);
    }

    @Test
    void Line_정보를_추가_실패_테스트_존재하는_이름으로_추가() {
        //given
        LineDetailEntity lineDetailEntity = new LineDetailEntity("3호선", "주황색");

        //when
        assertThatThrownBy(() -> lineDetailDao.insert(lineDetailEntity)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보를_수정할_수_있다() {
        //given
        LineDetailEntity lineDetailEntity = new LineDetailEntity("4호선", "노란색");
        LineDetailEntity insertLineDetailEntity = lineDetailDao.insert(lineDetailEntity);

        //when
        LineDetailEntity updateLineDetailEntity = new LineDetailEntity(insertLineDetailEntity.getId(), "5호선", "주황색");
        lineDetailDao.update(updateLineDetailEntity);

        //then
        LineDetailEntity findLineDetailEntity = lineDetailDao.findById(updateLineDetailEntity.getId());

        Assertions.assertThat(findLineDetailEntity).isEqualTo(findLineDetailEntity);
    }

    @Test
    void Line_정보_수정_실패_테스트_존재하는_이름으로_변경() {
        //given
        LineDetailEntity lineDetailEntity = new LineDetailEntity("4호선", "노란색");
        LineDetailEntity insertLineDetailEntity = lineDetailDao.insert(lineDetailEntity);

        //then
        LineDetailEntity updateLineDetailEntity = new LineDetailEntity(insertLineDetailEntity.getId(), "3호선", "주황색");
        assertThatThrownBy(() -> lineDetailDao.update(updateLineDetailEntity)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보_삭제_테스트() {
        //given
        LineDetailEntity lineDetailEntity = new LineDetailEntity("4호선", "노란색");
        LineDetailEntity insertLineDetailEntity = lineDetailDao.insert(lineDetailEntity);

        //then
        assertAll(
                () -> assertDoesNotThrow(() -> lineDetailDao.deleteById(insertLineDetailEntity.getId())),
                () -> assertThatThrownBy(() -> lineDetailDao.findById(insertLineDetailEntity.getId())).isInstanceOf(EmptyResultDataAccessException.class)
        );
    }

    @Test
    void Line_단일_검색_테스트() {
        //then
        LineDetailEntity lineDetailEntity = lineDetailDao.findById(1L);
        Assertions.assertThat(lineDetailEntity).isEqualTo(new LineDetailEntity(1L, "2호선", "초록색"));
    }

    @Test
    void Line_이름_검색_테스트() {
        //then
        Optional<LineDetailEntity> line = lineDetailDao.findByName("2호선");
        Assertions.assertThat(line).isPresent();
    }

    @Test
    void Line_이름_검색_실패_테스트() {
        //then
        Optional<LineDetailEntity> line = lineDetailDao.findByName("5호선");
        Assertions.assertThat(line).isEmpty();
    }

    @Test
    void Line_전체_검색_테스트() {
        //then
        List<LineDetailEntity> lineDetailEntities = lineDetailDao.findAll();
        Assertions.assertThat(lineDetailEntities).hasSize(2);
    }
}
