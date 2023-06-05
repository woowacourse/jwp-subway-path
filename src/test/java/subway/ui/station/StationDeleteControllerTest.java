package subway.ui.station;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.station.application.port.in.MockStationDeleteUseCase;
import subway.station.ui.StationDeleteController;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 id 통해 삭제하는 기능")
class StationDeleteControllerTest {

    private MockStationDeleteUseCase useCase;
    private StationDeleteController stationDeleteController;

    @BeforeEach
    void setUp() {
        useCase = new MockStationDeleteUseCase();
        stationDeleteController = new StationDeleteController(useCase);
    }

    @Test
    void 정상적으로_삭제된다() {
        final ResponseEntity<Void> result = stationDeleteController.deleteStation(1L);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastId()).isEqualTo(1L);
        });
    }
}
