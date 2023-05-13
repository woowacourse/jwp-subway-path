package subway.dao.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.domain.StationDomain;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StationDaoV2Test extends DaoTestConfig {

    StationDaoV2 stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDaoV2(jdbcTemplate);
    }

    @Test
    void 역_저장() {
        // when
        final Long saveStationId = stationDao.insert("잠실");

        // expect
        assertThat(saveStationId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 역_조회() {
        // given
        final Long saveStationId = stationDao.insert("잠실");

        // when
        final Optional<StationDomain> maybeStation = stationDao.findByStationId(saveStationId);

        assertThat(maybeStation).isPresent();

        // expect
        assertAll(
                () -> assertThat(maybeStation).isPresent(),
                () -> assertThat(maybeStation.get())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(new StationDomain(0L, "잠실"))
        );
    }
}
