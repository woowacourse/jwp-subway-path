package subway.ui.line.create;


import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.line.application.port.MockLineCreateUseCase;
import subway.line.ui.LineCreateController;
import subway.line.ui.dto.in.LineCreateRequest;
import subway.line.ui.dto.in.LineResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 생성하는 기능")
class LineCreateControllerTest {

    private MockLineCreateUseCase useCase;
    private LineCreateController lineCreateController;

    @BeforeEach
    void setUp() {
        useCase = new MockLineCreateUseCase();
        lineCreateController = new LineCreateController(useCase);
    }

    @Test
    void 정상적으로_생성된다() {
        // when
        ResponseEntity<LineResponse> result = lineCreateController.createLine(
                new LineCreateRequest("2호선", "초록색", 1L, 2L, 10L));

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            softly.assertThat(result.getBody().getName()).isEqualTo("2호선");
            softly.assertThat(result.getBody().getColor()).isEqualTo("초록색");
            softly.assertThat(result.getBody().getInterStations().get(0).getUpStationId()).isEqualTo(1L);
            softly.assertThat(result.getBody().getInterStations().get(0).getDownStationId()).isEqualTo(2L);
            softly.assertThat(result.getBody().getInterStations().get(0).getDistance()).isEqualTo(10);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
        });
    }
}
