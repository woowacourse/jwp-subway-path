package subway.ui.station;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.station.application.port.in.MockStationFindByIdUseCase;
import subway.station.ui.StationFindByIdController;
import subway.station.ui.dto.in.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 id를 통해 조회")
class StationFindByIdControllerTest {

    private MockStationFindByIdUseCase useCase;
    private StationFindByIdController stationFindByIdController;

    @BeforeEach
    void setUp() {
        useCase = new MockStationFindByIdUseCase();
        stationFindByIdController = new StationFindByIdController(useCase);
    }

    @Test
    void 정상적으로_조회된다() {
        final ResponseEntity<StationInfoResponse> result = stationFindByIdController.findStationInfoById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            softly.assertThat(result.getBody().getName()).isEqualTo("name");
            softly.assertThat(result.getBody().getId()).isOne();
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastId()).isEqualTo(1L);
        });
    }
}
