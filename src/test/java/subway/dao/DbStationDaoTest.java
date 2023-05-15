package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

@JdbcTest
class DbStationDaoTest {
    public static final StationEntity SAPYEONG_STATION_ENTITY = new StationEntity("사평역");
    public static final StationEntity EXPRESS_BUS_TERMINAL_STATION_ENTITY = new StationEntity("고속버스터미널역");
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
        final Station savedStation = stationDao.saveStation(SAPYEONG_STATION_ENTITY).toDomain();

        assertThat(savedStation.getId()).isGreaterThanOrEqualTo(1L);
        assertThat(savedStation.getName()).isEqualTo(SAPYEONG_STATION_ENTITY.getName());
    }

    @Test
    void findAll() {
        stationDao.saveStation(SAPYEONG_STATION_ENTITY);
        stationDao.saveStation(EXPRESS_BUS_TERMINAL_STATION_ENTITY);

        List<StationEntity> stationEntities = stationDao.findAll();

        assertThat(stationEntities.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        final Station savedStation = stationDao.saveStation(SAPYEONG_STATION_ENTITY).toDomain();

        final StationEntity foundStationEntity = stationDao.findById(savedStation.getId());

        assertThat(foundStationEntity).isEqualTo(savedStation.toEntity());
    }
}
