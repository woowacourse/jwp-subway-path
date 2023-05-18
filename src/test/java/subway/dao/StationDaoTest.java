package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("역을 저장한다.")
    @Test
    void save() {
        // given
        StationEntity stationEntity = new StationEntity(1L, "강남역");

        // when
        StationEntity newStationEntity = stationDao.save(stationEntity);

        // then
        assertThat(newStationEntity.getName()).isEqualTo("강남역");
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    void findAll() {
        // given
        StationEntity stationEntity1 = new StationEntity(1L, "강남역");
        StationEntity stationEntity2 = new StationEntity(2L, "역삼역");
        stationDao.save(stationEntity1);
        stationDao.save(stationEntity2);

        // when
        List<StationEntity> stationEntities = stationDao.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(stationEntities).hasSize(2);
            softly.assertThat(stationEntities.get(0).getName()).isEqualTo("강남역");
            softly.assertThat(stationEntities.get(1).getName()).isEqualTo("역삼역");
        });
    }

    @DisplayName("id로 해당 역을 조회한다.")
    @Test
    void findById() {
        // given
        StationEntity stationEntity = new StationEntity(1L, "강남역");
        StationEntity newStationEntity = stationDao.save(stationEntity);

        // when
        StationEntity foundStationEntity = stationDao.findById(newStationEntity.getId()).get();

        // then
        assertThat(foundStationEntity.getName()).isEqualTo("강남역");
    }

    @DisplayName("이름으로 해당 역을 조회힌다.")
    @Test
    void findByName() {
        // given
        String stationName = "강남역";
        StationEntity stationEntity = new StationEntity(1L, stationName);
        stationDao.save(stationEntity);

        // when
        StationEntity foundStationEntity = stationDao.findByName(stationName).get();

        // then
        assertThat(foundStationEntity.getName()).isEqualTo(stationName);
    }

    @DisplayName("id로 해당 역을 삭제한다.")
    @Test
    void deleteById() {
        // given
        StationEntity stationEntity = new StationEntity(1L, "강남역");
        StationEntity newStationEntity = stationDao.save(stationEntity);

        // when
        stationDao.deleteById(newStationEntity.getId());

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }
}
