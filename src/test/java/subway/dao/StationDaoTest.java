package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
class StationDaoTest {

    private StationDao stationDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("station을 추가한다.")
    void insert() {
        Station station = new Station("잠실역");

        assertDoesNotThrow(()->stationDao.insert(station));
    }

    @Test
    @DisplayName("id값으로 station을 조회한다.")
    void findById() {
        Station station = new Station("잠실역");
        Long stationId = stationDao.insert(station);

        Station findStation = stationDao.findById(stationId).get();

        assertAll(
                () -> assertThat(findStation.getId()).isEqualTo(stationId),
                () -> assertThat(findStation.getName()).isEqualTo("잠실역")
        );
    }

    @Test
    @DisplayName("id값으로 station을 삭제한다.")
    void deleteById() {
        Station station = new Station("잠실역");
        Long stationId = stationDao.insert(station);

        assertDoesNotThrow(()->stationDao.deleteById(stationId));
    }

    @Test
    @DisplayName("모든 Station을 찾는다.")
    void findAll(){
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실새내역");
        stationDao.insert(station1);
        stationDao.insert(station2);

        List<Station> stations = stationDao.findAll();

        assertThat(stations.size()).isEqualTo(2);
    }
}
