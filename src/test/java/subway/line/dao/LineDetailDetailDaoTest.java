package subway.line.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.domain.LineDetail;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineDetailDetailDaoTest {

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
        LineDetail lineDetail = new LineDetail("4호선", "노란색");

        //when
        LineDetail insertLineDetail = lineDetailDao.insert(lineDetail);

        //then
        Assertions.assertThat(insertLineDetail)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(lineDetail);
    }

    @Test
    void Line_정보를_추가_실패_테스트_존재하는_이름으로_추가() {
        //given
        LineDetail lineDetail = new LineDetail("3호선", "주황색");

        //when
        assertThatThrownBy(() -> lineDetailDao.insert(lineDetail)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보를_수정할_수_있다() {
        //given
        LineDetail lineDetail = new LineDetail("4호선", "노란색");
        LineDetail insertLineDetail = lineDetailDao.insert(lineDetail);

        //when
        LineDetail updateLineDetail = new LineDetail(insertLineDetail.getId(), "5호선", "주황색");
        lineDetailDao.update(updateLineDetail);

        //then
        LineDetail findLineDetail = lineDetailDao.findById(updateLineDetail.getId());

        Assertions.assertThat(findLineDetail).isEqualTo(findLineDetail);
    }

    @Test
    void Line_정보_수정_실패_테스트_존재하는_이름으로_변경() {
        //given
        LineDetail lineDetail = new LineDetail("4호선", "노란색");
        LineDetail insertLineDetail = lineDetailDao.insert(lineDetail);

        //then
        LineDetail updateLineDetail = new LineDetail(insertLineDetail.getId(), "3호선", "주황색");
        assertThatThrownBy(() -> lineDetailDao.update(updateLineDetail)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void Line_정보_삭제_테스트() {
        //given
        LineDetail lineDetail = new LineDetail("4호선", "노란색");
        LineDetail insertLineDetail = lineDetailDao.insert(lineDetail);

        //then
        assertAll(
                () -> assertDoesNotThrow(() -> lineDetailDao.deleteById(insertLineDetail.getId())),
                () -> assertThatThrownBy(() -> lineDetailDao.findById(insertLineDetail.getId())).isInstanceOf(EmptyResultDataAccessException.class)
        );
    }

    @Test
    void Line_단일_검색_테스트() {
        //then
        LineDetail lineDetail = lineDetailDao.findById(1L);
        Assertions.assertThat(lineDetail).isEqualTo(new LineDetail(1L, "2호선", "초록색"));
    }

    @Test
    void Line_이름_검색_테스트() {
        //then
        Optional<LineDetail> line = lineDetailDao.findByName("2호선");
        Assertions.assertThat(line).isPresent();
    }

    @Test
    void Line_이름_검색_실패_테스트() {
        //then
        Optional<LineDetail> line = lineDetailDao.findByName("5호선");
        Assertions.assertThat(line).isEmpty();
    }

    @Test
    void Line_전체_검색_테스트() {
        //then
        List<LineDetail> lineDetails = lineDetailDao.findAll();
        Assertions.assertThat(lineDetails).hasSize(2);
    }
}
