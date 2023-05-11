package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.StationEntity;

import java.util.Optional;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Station을 저장한다.")
    void insertTest() {
        // given
        StationEntity insertEntity = 강변_INSERT_ENTITY;

        // when
        Long insertedStationId = stationDao.insert(insertEntity);

        // then
        assertThat(insertedStationId).isEqualTo(3L);
    }

    @Test
    @DisplayName("받은 역 이름과 노선 이름에 해당하는 행을 조회한다.")
    void findByStationNameAndLineNameTest() {
        // given
        String stationName = DUMMY_STATION_건대역_NAME;
        String lineName = DUMMY_LINE2_NAME;

        // when
        Optional<StationEntity> findStation = stationDao.findByStationNameAndLineName(stationName, lineName);

        // then
        assertThat(findStation.get()).isEqualTo(건대_FIND_ENTITY);
    }
}