package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("역을 추가한다.")
    void insert() {
        StationEntity stationEntity = new StationEntity(null, "부산역");

        StationEntity savedStationEntity = stationDao.insert(stationEntity);

        assertAll(
                () -> assertThat(savedStationEntity.getId()).isNotNull(),
                () -> assertThat(savedStationEntity.getName()).isEqualTo(stationEntity.getName())
        );
    }

    @Test
    @DisplayName("ID에 해당하는 역을 가져온다.")
    void findById() {
        StationEntity stationEntity = new StationEntity(null, "부산역");
        StationEntity savedStationEntity = stationDao.insert(stationEntity);

        StationEntity findStationEntity = stationDao.findById(savedStationEntity.getId()).get();

        assertThat(findStationEntity).isEqualTo(savedStationEntity);
    }

    @Test
    @DisplayName("이름에 해당하는 역을 가져온다.")
    void findByName() {
        StationEntity stationEntity = new StationEntity(null, "부산역");
        StationEntity savedStationEntity = stationDao.insert(stationEntity);

        StationEntity findStationEntity = stationDao.findByName(savedStationEntity.getName()).get();

        assertThat(findStationEntity).isEqualTo(savedStationEntity);
    }

    @Test
    @DisplayName("모든 역을 가져온다.")
    void findAll() {
        StationEntity stationEntity1 = new StationEntity(null, "부산역");
        StationEntity stationEntity2 = new StationEntity(null, "서면역");
        StationEntity savedStationEntity1 = stationDao.insert(stationEntity1);
        StationEntity savedStationEntity2 = stationDao.insert(stationEntity2);

        List<StationEntity> stations = stationDao.findAll();

        assertThat(stations).containsExactly(savedStationEntity1, savedStationEntity2);
    }

    @Test
    @DisplayName("ID에 해당하는 역을 삭제한다.")
    void deleteById() {
        StationEntity stationEntity = new StationEntity(null, "부산역");
        StationEntity savedStationEntity = stationDao.insert(stationEntity);

        stationDao.deleteById(savedStationEntity.getId());

        assertThat(stationDao.findById(savedStationEntity.getId()).isEmpty()).isTrue();
    }
}
