package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
    private Long id;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);

        id = stationDao.save(new StationEntity("잠실역"));
    }

    @Test
    @DisplayName("Station id를 입력받아 해당하는 Station Entity 를 반환한다.")
    void findById() {
        // when
        StationEntity newStationEntity = stationDao.findById(id);

        // expected
        assertThat(newStationEntity.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("저장한다.")
    void save() {
        // when
        Long newId = stationDao.save(new StationEntity("잠실새내역"));

        // expected
        assertThat(newId).isEqualTo(id + 1);
    }

    @Test
    @DisplayName("Station 이름을 입력받아 해당하는 Station Entity 를 반환한다.")
    void findByName() {
        // when
        StationEntity newStationEntity = stationDao.findByName("잠실역");

        // expected
        assertThat(newStationEntity.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Station 이름을 입력받아 일치하는 역을 삭제한다.")
    void deleteByName() {
        // when
        int deleteRowNumber = stationDao.deleteByName("잠실역");

        // expected
        assertThat(deleteRowNumber).isEqualTo(1);
    }
}
