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
import subway.entity.StationEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 여러_Station을_저장한다() {
        // given
        List<StationEntity> stations = List.of(new StationEntity("강남역"), new StationEntity("역삼역"));

        // when
        stationDao.insertAll(stations);

        // then
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    void 역이_존재하면_true를_반환한다() {
        // given
        stationDao.insert(new StationEntity("강남역"));

        // when
        final boolean exists = stationDao.existsByName("강남역");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 역이_존재하지_않으면_false를_반환한다() {
        // given
        stationDao.insert(new StationEntity("강남역"));

        // when
        final boolean exists = stationDao.existsByName("역삼역");

        // then
        assertThat(exists).isFalse();
    }
}
