package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.domain.Line;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LineDaoTest extends DaoTestConfig {

    LineDao lineDao;

    SectionDao sectionDao;

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        // when
        final Long 노선_2_식별자값 = lineDao.save("2", "초록");

        final Optional<Line> 아마도_노선_2 = lineDao.findById(노선_2_식별자값);
        assertThat(아마도_노선_2).isPresent();
        final Line 노선_2 = 아마도_노선_2.get();

        // then
        assertThat(노선_2)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Line("2", "초록"));
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    void findAll() {
        // given
        lineDao.save("1", "파랑");
        lineDao.save("2", "초록");

        // when
        final List<Line> 전체_노선_목록 = lineDao.findAll();

        // then
        assertThat(전체_노선_목록)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(
                        new Line("1", "파랑"),
                        new Line("2", "초록")
                );
    }

    @DisplayName("노선_식별자값을 통해 노선을 조회한다.")
    @Test
    void findById() {
        // given
        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");

        // when
        final Optional<Line> 아마도_노선_1 = lineDao.findById(노선_1_식별자값);

        assertThat(아마도_노선_1).isPresent();
        final Line 노선_1 = 아마도_노선_1.get();

        // then
        assertThat(노선_1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Line("1", "파랑"));
    }

    @DisplayName("노선_이름을 통해 노선을 조회한다.")
    @Test
    void findByName() {
        // given
        lineDao.save("1", "파랑");

        // when
        final Optional<Line> 아마도_노선_1 = lineDao.findByName("1");

        assertThat(아마도_노선_1).isPresent();
        final Line 노선_1 = 아마도_노선_1.get();

        // then
        assertThat(노선_1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Line("1", "파랑"));
    }

    @DisplayName("노선_식별자값을 통해 노선을 삭제한다.")
    @Test
    void delete() {
        // given
        final Long 노선_1_식별자값 = lineDao.save("1", "파랑");

        // when
        lineDao.deleteById(노선_1_식별자값);

        final Optional<Line> 아마도_노선_1 = lineDao.findById(노선_1_식별자값);

        // then
        assertThat(아마도_노선_1).isEmpty();
    }
}
