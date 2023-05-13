package subway.dao;

import static fixtures.LineFixtures.Line2;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.station.Station;

@SpringBootTest
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
    @DisplayName("역을 저장한다.")
    void insertTest() {
        // given
        Station insertEntity = STATION_B.INSERT_ENTITY;

        // when
        Station insertedStation = stationDao.insert(insertEntity);

        // then
        assertThat(insertedStation).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(STATION_B.FIND_ENTITY);
    }

    @DisplayName("역 ID로 행을 조회한다.")
    @Test
    void findById() {
        // given
        long stationId = STATION_A.ID;

        // when
        Optional<Station> findStation = stationDao.findById(stationId);

        // then
        assertThat(findStation.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(STATION_A.FIND_ENTITY);
    }

    @Test
    @DisplayName("역 ID에 해당하는 행이 없으면 빈 Optional을 반환한다.")
    void findByIdEmptyOptional() {
        // given
        long stationId = STATION_B.ID;

        // when
        Optional<Station> findStation = stationDao.findById(stationId);

        // then
        assertThat(findStation.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("역 이름과 노선 이름에 해당하는 행을 조회한다.")
    void findByStationNameAndLineNameTest() {
        // given
        String stationName = STATION_C.NAME;
        String lineName = Line2.NAME;

        // when
        Optional<Station> findStation = stationDao.findByStationNameAndLineName(stationName, lineName);

        // then
        assertThat(findStation.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(STATION_C.FIND_ENTITY);
    }

    @ParameterizedTest
    @CsvSource(value = {"A역:1호선", "B역:2호선", "D역:3호선"}, delimiter = ':')
    @DisplayName("역 이름과 노선 이름에 해당하는 행이 없으면 빈 Optional을 반환한다.")
    void findByStationNameAndLineNameEmptyOptional(String stationName, String lineName) {
        // when
        Optional<Station> findStation = stationDao.findByStationNameAndLineName(stationName, lineName);

        // then
        assertThat(findStation.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1:False", "3:True"}, delimiter = ':')
    @DisplayName("역 ID에 해당하는 행이 있으면 False, 없으면 True를 반환한다.")
    void isNotExistById(String id, Boolean expected) {
        // given
        Long parsedId = Long.parseLong(id);

        // when, then
        assertThat(stationDao.isNotExistById(parsedId)).isEqualTo(expected);
    }

    @Test
    @DisplayName("역 ID에 해당하는 행을 삭제한다.")
    void deleteById() {
        // given
        long stationId = STATION_A.ID;

        // when
        stationDao.deleteById(stationId);
        Optional<Station> findStation = stationDao.findById(stationId);

        // then
        assertThat(findStation.isEmpty()).isTrue();
    }
}
