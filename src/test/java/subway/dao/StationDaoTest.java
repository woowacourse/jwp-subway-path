package subway.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StationDaoTest extends DaoTestConfig {

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역_저장() {
        // when
        final Long saveStationId = stationDao.insert(new StationEntity("잠실"));

        // expect
        assertThat(saveStationId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 역_조회() {
        // given
        final Long saveStationId = stationDao.insert(new StationEntity("잠실"));

        // when
        final Optional<StationEntity> maybeStation = stationDao.findByStationId(saveStationId);

        // expect
        assertAll(
                () -> assertThat(maybeStation).isPresent(),
                () -> assertThat(maybeStation.get())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(new StationEntity(0L, "잠실"))
        );
    }

    @Test
    void 역_식별자값_목록으로_역들을_조회한다() {
        // given
        final Long saveStationId1 = stationDao.insert(new StationEntity("잠실"));
        final Long saveStationId2 = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveStationId3 = stationDao.insert(new StationEntity("잠실새내"));

        // when
        final List<StationEntity> findStations = stationDao.findInStationIds(List.of(saveStationId1, saveStationId2, saveStationId3));

        // then
        Assertions.assertThat(findStations)
                .containsExactly(
                        new StationEntity(saveStationId1, "잠실"),
                        new StationEntity(saveStationId2, "잠실나루"),
                        new StationEntity(saveStationId3, "잠실새내")
                );
    }

    @Test
    void 역_이름으로_역을_조회한다() {
        // given
        final Long saveStationId = stationDao.insert(new StationEntity("잠실"));

        // when
        final Optional<StationEntity> maybeStationEntity = stationDao.findByStationName("잠실");

        // then
        assertAll(
                () -> assertThat(maybeStationEntity).isPresent(),
                () -> assertThat(maybeStationEntity.get())
                        .isEqualTo(new StationEntity(saveStationId, "잠실"))
        );
    }
}
