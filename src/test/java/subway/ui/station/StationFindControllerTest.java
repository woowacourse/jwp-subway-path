package subway.ui.station;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.station.application.port.in.MockStationFindAllUseCase;
import subway.station.ui.StationFindController;
import subway.station.ui.dto.in.StationInfosResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 전체 조회")
class StationFindControllerTest {

    private MockStationFindAllUseCase useCase;
    private StationFindController stationFindController;

    @BeforeEach
    void setUp() {
        useCase = new MockStationFindAllUseCase();
        stationFindController = new StationFindController(useCase);
    }

    @Test
    void 정상적으로_조회된다() {
        // when
        final ResponseEntity<StationInfosResponse> result = stationFindController.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            softly.assertThat(result.getBody().getStations().size()).isEqualTo(2);
            softly.assertThat(result.getBody().getStations().get(0).getName()).isEqualTo("name1");
            softly.assertThat(result.getBody().getStations().get(0).getId()).isOne();
            softly.assertThat(result.getBody().getStations().get(1).getName()).isEqualTo("name2");
            softly.assertThat(result.getBody().getStations().get(1).getId()).isEqualTo(2L);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
        });
    }
}
