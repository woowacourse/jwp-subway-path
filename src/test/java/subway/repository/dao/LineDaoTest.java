package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.repository.entity.LineEntity;

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
        assertThat(lineDao.findById(saveLine.getId())).isPresent();
    }

    @Test
    void 모든_노선을_조회한다() {
        //given
        lineDao.insert(new LineEntity("1호선"));
        lineDao.insert(new LineEntity("2호선"));
        lineDao.insert(new LineEntity("3호선"));

        //when
        List<LineEntity> findLines = lineDao.findAll();

        //then
        assertThat(findLines).hasSize(3);
    }

    @Test
    void 노선_ID로_삭제한다() {
        //given
        LineEntity lineEntity = lineDao.insert(new LineEntity("1호선"));
        lineDao.insert(new LineEntity("2호선"));
        lineDao.insert(new LineEntity("3호선"));

        //when
        lineDao.deleteById(lineEntity.getId());

        //then
        assertThat(lineDao.findAll()).hasSize(2);
    }


    @Test
    void 노선_이름으로_조회한다() {
        //given
        lineDao.insert(new LineEntity("1호선"));
        lineDao.insert(new LineEntity("2호선"));
        lineDao.insert(new LineEntity("3호선"));

        //when, then
        assertThat(lineDao.findByName("1호선")).isPresent();
    }

    @Test
    void 노선_이름이_존재하면_true를_반환한다() {
        //given
        lineDao.insert(new LineEntity("1호선"));

        //when, then
        assertThat(lineDao.existsByName("1호선")).isTrue();
    }


    @Test
    void 노선_이름이_존재하지_않으면_false를_반환한다() {
        //given
        lineDao.insert(new LineEntity("2호선"));

        //when, then
        assertThat(lineDao.existsByName("1호선")).isFalse();
    }
}
