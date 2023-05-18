package subway.adapter.out.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.adapter.out.persistence.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class StationJdbcDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationJdbcDao stationJdbcDao;

    @BeforeEach
    void setUp() {
        stationJdbcDao = new StationJdbcDao(jdbcTemplate);
    }

    @Test
    @DisplayName("역을 추가 한다.")
    void createStation() {
        StationEntity stationEntity = new StationEntity("비버");

        stationJdbcDao.createStation(stationEntity);

        assertThat(stationJdbcDao.findAll().get(0).getName()).isEqualTo("비버");
    }

    @Test
    @DisplayName("전체 역을 조회한다.")
    void findAll() {
        StationEntity stationEntity1 = new StationEntity("비버");
        StationEntity stationEntity2 = new StationEntity("라빈");
        Long station1 = stationJdbcDao.createStation(stationEntity1);
        Long station2 = stationJdbcDao.createStation(stationEntity2);

        List<StationEntity> result = stationJdbcDao.findAll();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new StationEntity(station2, stationEntity2.getName()),
                        new StationEntity(station1, stationEntity1.getName())));
    }

    @Test
    @DisplayName("역을 삭제한다.")
    void deleteById() {
        StationEntity stationEntity = new StationEntity("비버");
        Long stationId = stationJdbcDao.createStation(stationEntity);

        stationJdbcDao.deleteById(stationId);

        assertThat(stationJdbcDao.findAll()).hasSize(0);
    }
}