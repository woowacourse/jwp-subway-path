package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

import java.util.List;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBStationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        stationRepository = new DBStationRepository(jdbcTemplate, stationDao);
    }

    @Test
    @DisplayName("Station을 entity로 변환하여 저장한 후 저장된 entity를 Station으로 만들어서 반환한다.")
    void insertTest() {
        // given
        Station station = STATION_TO_INSERT_강변역;
        Station expectStation = STATION_강변역;

        // when
        Station insertedStation = stationRepository.insert(station);

        // then
        assertThat(insertedStation).isEqualTo(expectStation);
    }

    @Test
    @DisplayName("주어진 역 id에 해당하는 역을 Station 도메인으로 만들어서 가져온다.")
    void findStationByIdTest() {
        // given
        Long stationId = STATION_잠실역_ID;

        // when
        Station findStation = stationRepository.findStationById(stationId);

        // then
        assertThat(findStation).isEqualTo(STATION_잠실역);
    }

    @Test
    @DisplayName("주어진 lineId에 해당하는 역들을 가져온다.")
    void findStationsByLineIdTest() {
        // given
        Long lineId = LINE2_ID;
        List<Station> expectStations = List.of(STATION_잠실역, STATION_건대역);

        // when
        List<Station> findStations = stationRepository.findStationsByLineId(lineId);

        // then
        assertThat(findStations).isEqualTo(expectStations);
    }

    @Test
    @DisplayName("주어진 역을 삭제한다.")
    void removeTest() {
        // given
        Station station = STATION_잠실역;

        // when
        stationRepository.remove(station);

        // then
        assertThat(stationRepository.findStationsByLineId(station.getLineId())).hasSize(1);
    }
}