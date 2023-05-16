package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
        StationEntity stationEntity = new StationEntity("잠실역");

        assertDoesNotThrow(()->stationDao.insert(stationEntity));
    }

    @Test
    @DisplayName("id값으로 station을 조회한다.")
    void findById() {
        StationEntity stationEntity = new StationEntity("잠실역");
        Long stationId = stationDao.insert(stationEntity);

        StationEntity station = stationDao.findById(stationId).get();

        assertAll(
                () -> assertThat(station.getId()).isEqualTo(stationId),
                () -> assertThat(station.getName()).isEqualTo("잠실역")
        );
    }

    @Test
    @DisplayName("id값으로 station을 삭제한다.")
    void deleteById() {
        StationEntity stationEntity = new StationEntity("잠실역");
        Long stationId = stationDao.insert(stationEntity);

        assertDoesNotThrow(()->stationDao.deleteById(stationId));
    }

    @Test
    @DisplayName("모든 Station을 찾는다.")
    void findAll(){
        StationEntity stationEntity1 = new StationEntity("잠실역");
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        stationDao.insert(stationEntity1);
        stationDao.insert(stationEntity2);

        List<StationEntity> stations = stationDao.findAll();

        assertThat(stations.size()).isEqualTo(2);
    }
}
