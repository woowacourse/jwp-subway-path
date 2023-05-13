package subway.service.subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.subway.Station;
import subway.dto.station.LineMapResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.service.SubwayMapService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.SectionsFixture.createSections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class SubwayMapServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SubwayMapService subwayMapService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("지하철역의 역 정보를 순서대로 보여준다.")
    void show_ordered_station_map_success() {
        // given
        stationRepository.insertStation(new Station("잠실역"));
        stationRepository.insertStation(new Station("잠실새내역"));
        stationRepository.insertStation(new Station("종합운동장역"));

        lineRepository.insertLine(new LineEntity(1L, 2, "2호선", "초록색"));
        lineRepository.updateLine(createSections(), 2);

        // when
        LineMapResponse result = subwayMapService.showLineMap(2);

        // then
        assertAll(
                () -> assertThat(result.getStations().size()).isEqualTo(3),
                () -> assertThat(result.getStations().get(0).getName()).isEqualTo("잠실역"),
                () -> assertThat(result.getStations().get(2).getName()).isEqualTo("종합운동장역")
        );
    }
}
