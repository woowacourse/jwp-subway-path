package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.StationEntity;

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
    void 이름으로_저장한다() {
        // when
        final StationEntity insertedStationId = stationDao.insert("잠실역");
        final Long id = insertedStationId.getId();

        // then
        final Optional<StationEntity> stationEntity = stationDao.findById(id);
        assertAll(
                () ->assertThat(stationEntity).isPresent(),
                () ->assertThat(stationEntity.get().getName()).isEqualTo("잠실역")
        );
    }

    @Test
    void 이름으로_조회할_수_있다() {
        // given
        final StationEntity insertedStationId = stationDao.insert("잠실역");
        final Long id = insertedStationId.getId();

        // when
        final Optional<StationEntity> stationEntity = stationDao.findByName("잠실역");

        // then
        assertThat(stationEntity).isPresent();
    }

}
