package subway.service.station;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.subway.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.exception.NameIsBlankException;
import subway.repository.StationRepository;
import subway.service.StationService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class StationServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("역을 저장한다.")
    void save_section_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");

        // when
        stationService.saveStation(stationCreateRequest);

        // then
        StationsResponse expected = stationService.findAllStationResponses();

        assertAll(
                () -> assertThat(expected.getStations().size()).isEqualTo(1),
                () -> assertThat(expected.getStations().get(0).getName()).isEqualTo(stationCreateRequest.getName())
        );
    }

    @Test
    @DisplayName("역의 이름이 공백이면 예외를 발생시킨다.")
    void throws_exception_when_station_name_is_blank() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("");

        // when & then
        assertThatThrownBy(() -> stationService.saveStation(stationCreateRequest))
                .isInstanceOf(NameIsBlankException.class);
    }

    @Test
    @DisplayName("역을 찾는다.")
    void find_station_success() {
        // given
        Station station = new Station("잠실역");
        Long id = stationRepository.insertStation(station);

        // when
        StationResponse result = stationService.findStationEntityById(id);

        // then
        assertThat(result.getName()).isEqualTo(station.getName());
    }

    @Test
    @DisplayName("역을 모두 찾는다.")
    void find_stations_success() {
        // given
        Station station = new Station("잠실역");
        stationRepository.insertStation(station);

        // when
        StationsResponse result = stationService.findAllStationResponses();

        // then
        assertAll(
                () -> assertThat(result.getStations().size()).isEqualTo(1),
                () -> assertThat(result.getStations().get(0).getName()).isEqualTo(station.getName())
        );
    }
}
