package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

@JdbcTest
class DbStationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new DbStationDao(jdbcTemplate, dataSource);
    }

    @Test
    void saveStationTest() {
        final Station savedStation = stationDao.saveStation(SAPYEONG_STATION);

        assertThat(savedStation.getId()).isGreaterThanOrEqualTo(1L);
        assertThat(savedStation.getName()).isEqualTo(SAPYEONG_STATION.getName());
    }

    @Test
    void findAll() {
        stationDao.saveStation(SAPYEONG_STATION);
        stationDao.saveStation(EXPRESS_BUS_TERMINAL_STATION);

        List<Station> stations = stationDao.findAll();

        assertThat(stations.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        final Station savedStation = stationDao.saveStation(SAPYEONG_STATION);

        final Station foundStation = stationDao.findById(savedStation.getId());

        assertThat(foundStation).isEqualTo(savedStation);
    }
}
