package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.dao.entity.StationEntity;

@JdbcTest
@ActiveProfiles("test")
class StationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 역을 저장한다.")
    void insert() {
        // given
        StationEntity stationEntity = new StationEntity("정자역");

        // when
        Long savedId = stationDao.insert(stationEntity);

        // then
        Optional<StationEntity> foundStation = stationDao.findById(savedId);
        assertThat(foundStation).isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(new StationEntity(savedId, "정자역"));
    }

    @Test
    @DisplayName("저장된 모든 역 정보를 조회한다.")
    void findAll() {
        // given
        stationDao.insert(new StationEntity("정자역"));
        stationDao.insert(new StationEntity("미금역"));

        // when, then
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("이름으로 역 정보를 조회한다.")
    void findByName() {
        // given
        Long savedId = stationDao.insert(new StationEntity("정자역"));

        // when
        Optional<StationEntity> foundStation = stationDao.findByName("정자역");

        // then
        assertThat(foundStation).isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(new StationEntity(savedId, "정자역"));
    }

    @Test
    @DisplayName("역 이름을 변경할 수 있다.")
    void update() {
        // given
        Long savedId = stationDao.insert(new StationEntity("미금역"));
        StationEntity changedEntity = new StationEntity(savedId, "정자역");

        // when
        stationDao.update(changedEntity);

        // then
        assertThat(stationDao.findById(savedId)).isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(changedEntity);

    }

    @Test
    @DisplayName("역을 삭제할 수 있다.")
    void deleteById() {
        // given
        Long savedId = stationDao.insert(new StationEntity("미금역"));
        stationDao.deleteById(savedId);

        // when
        stationDao.deleteById(savedId);

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }

}
