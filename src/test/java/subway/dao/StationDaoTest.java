package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
        stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("Station을 저장한다.")
    @Test
    void save() {
        // when
        Long id = stationDao.save(new StationEntity("잠실새내역"));

        // then
        assertThat(stationDao.findByName("잠실새내역").getId()).isEqualTo(id);
    }

    @DisplayName("Station 이름을 입력받아 해당하는 Station Entity 를 반환한다.")
    @Test
    void findByName() {
        // given
        Long id = stationDao.save(new StationEntity("잠실새내역"));

        // when
        StationEntity newStationEntity = stationDao.findByName("잠실새내역");

        // then
        assertThat(newStationEntity.getId()).isEqualTo(id);
    }

    @DisplayName("전체 Station을 반환한다.")
    @Test
    void findAll() {
        // given
        stationDao.save(new StationEntity("잠실새내역"));
        stationDao.save(new StationEntity("찰리역"));
        stationDao.save(new StationEntity("토리역"));

        // when
        List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertThat(stationEntities).hasSize(3);
    }

    @DisplayName("중복되는 이름인지 확인한다.")
    @Test
    void isExisted() {
        // given
        stationDao.save(new StationEntity("잠실역"));

        // when
        boolean duplicate = stationDao.isExisted("잠실역");

        // then
        assertThat(duplicate).isTrue();
    }
}
