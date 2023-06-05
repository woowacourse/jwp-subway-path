package subway.ui.station;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.station.application.port.in.MockStationCreateUseCase;
import subway.station.ui.StationCreateController;
import subway.station.ui.dto.in.StationCreateRequest;
import subway.station.ui.dto.in.StationInfoResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역을 생성하는 기능")
class StationCreateControllerTest {

    private MockStationCreateUseCase useCase;
    private StationCreateController stationCreateController;

    @BeforeEach
    void setUp() {
        useCase = new MockStationCreateUseCase();
        stationCreateController = new StationCreateController(useCase);
    }

    @Test
    void 정상적으로_생성된다() {
        // when
        final ResponseEntity<StationInfoResponse> result = stationCreateController.create(
            new StationCreateRequest("강남역"));

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            softly.assertThat(result.getBody().getName()).isEqualTo("강남역");
            softly.assertThat(result.getBody().getId()).isOne();
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
        });
    }
}
