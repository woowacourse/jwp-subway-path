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
    private Long id;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);

        id = stationDao.save(new StationEntity("잠실역"));
    }

    @DisplayName("저장한다.")
    @Test
    void save() {
        // when
        Long newId = stationDao.save(new StationEntity("잠실새내역"));

        // then
        assertThat(newId).isEqualTo(id + 1);
    }

    @DisplayName("Station id를 입력받아 해당하는 Station Entity 를 반환한다.")
    @Test
    void findById() {
        // when
        StationEntity newStationEntity = stationDao.findById(id);

        // then
        assertThat(newStationEntity.getId()).isEqualTo(id);
    }

    @DisplayName("Station 이름을 입력받아 해당하는 Station Entity 를 반환한다.")
    @Test
    void findByName() {
        // when
        StationEntity newStationEntity = stationDao.findByName("잠실역");

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
        assertThat(stationEntities).hasSize(4);
    }

    @Test
    @DisplayName("Station 이름을 입력받아 일치하는 역을 삭제한다.")
    void deleteByName() {
        // when
        int deleteRowNumber = stationDao.deleteByName("잠실역");

        // then
        assertThat(deleteRowNumber).isEqualTo(1);
    }

    @DisplayName("중복되는 이름인지 확인한다.")
    @Test
    void isExisted() {
        // when
        boolean duplicate = stationDao.isExisted("잠실역");

        // then
        assertThat(duplicate).isTrue();
    }
}
