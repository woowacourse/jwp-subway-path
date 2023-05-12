package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StationDaoTest extends DaoTestConfig {

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("지하철역을 저장한다.")
    @Test
    void save() {
        // when
        final Long 잠실역_식별자값 = stationDao.save("잠실");

        final Optional<Station> 아마도_잠실역 = stationDao.findById(잠실역_식별자값);

        assertThat(아마도_잠실역).isPresent();
        final Station 잠실역 = 아마도_잠실역.get();

        // expect
        assertThat(잠실역)
                .usingRecursiveComparison()
                .ignoringFields("id", "sections")
                .isEqualTo(new Station(0L, "잠실", Collections.emptyList()));
    }

    @DisplayName("전체 지하철역을 조회한다.")
    @Test
    void findAll() {
        // given
        stationDao.save("잠실");
        stationDao.save("잠실새내");

        // when
        final List<Station> 전체_역 = stationDao.findAll();

        // then
        assertThat(전체_역)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "sections")
                .containsExactly(
                        new Station(0L, "잠실", Collections.emptyList()),
                        new Station(0L, "잠실새내", Collections.emptyList())
                );
    }

    @DisplayName("지하철역_식별자값으로 지하철역을 조회한다.")
    @Test
    void findById() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");

        // when
        final Optional<Station> 아마도_잠실역 = stationDao.findById(잠실역_식별자값);

        assertThat(아마도_잠실역).isPresent();
        final Station 잠실 = 아마도_잠실역.get();

        // then
        assertThat(잠실)
                .usingRecursiveComparison()
                .ignoringFields("id", "sections")
                .isEqualTo(new Station(0L, "잠실", Collections.emptyList()));
    }

    @DisplayName("지하철역_이름으로 지하철역을 조회한다.")
    @Test
    void findByName() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");

        // when
        final Optional<Station> 아마도_잠실역 = stationDao.findByName("잠실");

        assertThat(아마도_잠실역).isPresent();
        final Station 잠실 = 아마도_잠실역.get();

        // then
        assertThat(잠실)
                .usingRecursiveComparison()
                .ignoringFields("id", "sections")
                .isEqualTo(new Station(0L, "잠실", Collections.emptyList()));
    }

    @DisplayName("지하철역_식별자값으로 지하철역을 삭제한다.")
    @Test
    void deleteById() {
        // given
        final Long 잠실역_식별자값 = stationDao.save("잠실");

        // when
        stationDao.deleteById(잠실역_식별자값);

        final Optional<Station> 아마도_잠실역 = stationDao.findById(잠실역_식별자값);

        // then
        assertThat(아마도_잠실역).isEmpty();
    }
}
