package subway.ui.station;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.station.application.port.in.MockStationUpdateInfoUseCase;
import subway.station.ui.StationUpdateInfoController;
import subway.station.ui.dto.in.StationUpdateInfoRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철역 정보 수정")
class StationUpdateInfoControllerTest {

    private MockStationUpdateInfoUseCase useCase;
    private StationUpdateInfoController stationUpdateInfoController;

    @BeforeEach
    void setUp() {
        useCase = new MockStationUpdateInfoUseCase();
        stationUpdateInfoController = new StationUpdateInfoController(useCase);
    }


    @Test
    void 정상적으로_수정된다() {
        // given
        final StationUpdateInfoRequest request = new StationUpdateInfoRequest("강남역");

        // when
        final ResponseEntity<Void> result = stationUpdateInfoController.updateStationInfo(1L, request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastRequestDto().getName()).isEqualTo("강남역");
        });
    }
}
