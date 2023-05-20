package subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Sql("/InitializeTable.sql")
@JdbcTest
class StationDaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("특정 이름을 가진 역이 있는지 확인한다.")
    void checkExistenceByName() {
        assertAll(
            () -> assertThat(stationDao.checkExistenceByName("신도림")).isTrue(),
            () -> assertThat(stationDao.checkExistenceByName("강동")).isFalse()
        );
    }

    @Test
    @DisplayName("특정 id를 가진 역이 있는지 확인한다.")
    void checkExistenceById() {
        assertAll(
            () -> assertThat(stationDao.checkExistenceById(1L)).isTrue(),
            () -> assertThat(stationDao.checkExistenceById(5L)).isFalse());
    }

    @Test
    @DisplayName("특정 id를 가진 역이 있는지 확인한다.")
    void findStationByList() {
        List<Long> stationIds = List.of(1L, 3L);

        assertAll(
            () -> assertThat(stationDao.findStationByList(stationIds).get(0).getName()).isEqualTo("신도림"),
            () -> assertThat(stationDao.findStationByList(stationIds).get(1).getName()).isEqualTo("신대방"));
    }
}
