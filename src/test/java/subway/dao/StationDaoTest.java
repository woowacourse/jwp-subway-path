package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.station.Station;

@JdbcTest
class StationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Station을 저장할 수 있어야 한다.")
    void insert_success() {
        // given
        Station station = new Station("잠실역");

        // when
        stationDao.insert(station);

        // then
        assertThat(stationDao.existsBy("잠실역"))
                .isTrue();
    }

    @Test
    @DisplayName("Station을 수정할 수 있어야 한다.")
    void update_success() {
        // given
        insertStation("잠실역");

        // when
        stationDao.update("잠실역", new Station("잠실나루역"));

        // then
        assertThat(stationDao.doesNotExistBy("잠실역"))
                .isTrue();
        assertThat(stationDao.existsBy("잠실나루역"))
                .isTrue();
    }

    @Test
    @DisplayName("모든 Station을 조회할 수 있어야 한다.")
    void findAll_success() {
        // given
        insertStation("잠실역");
        insertStation("잠실나루역");
        insertStation("잠실새내역");

        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertThat(stations)
                .hasSize(3);
    }

    @Test
    @DisplayName("이름으로 Station을 조회할 수 있어야 한다.")
    void findByName_success() {
        // given
        insertStation("잠실역");

        // when
        Optional<Station> station = stationDao.findByName("잠실역");

        // then
        assertThat(station)
                .isPresent();
    }

    @Test
    @DisplayName("없는 이름의 Station을 조회하면 빈 Optional이 반환되어야 한다.")
    void findByName_notExistStation() {
        // given
        insertStation("잠실역");

        // when
        Optional<Station> notExistStation = stationDao.findByName("잠실나루역");

        // then
        assertThat(notExistStation)
                .isEmpty();
    }

    @Test
    @DisplayName("Station을 삭제할 수 있어야 한다.")
    void deleteByName_success() {
        // given
        insertStation("잠실역");

        // when
        stationDao.deleteByName("잠실역");

        // then
        assertThat(stationDao.doesNotExistBy("잠실역"))
                .isTrue();
    }


    private void insertStation(String stationName) {
        Station station = new Station(stationName);
        stationDao.insert(station);
    }
}
