package subway.ui.line;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.application.line.port.MockLineFindByIdUseCase;
import subway.application.line.port.in.InterStationResponseDto;
import subway.application.line.port.in.LineResponseDto;
import subway.ui.line.dto.in.InterStationResponse;
import subway.ui.line.dto.in.LineResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 조회하는 기능")
class LineFindByIdControllerTest {

    private MockLineFindByIdUseCase useCase;
    private LineFindByIdController lineFindByIdController;

    @BeforeEach
    void setUp() {
        useCase = new MockLineFindByIdUseCase();
        lineFindByIdController = new LineFindByIdController(useCase);
    }

    @Test
    void 정상적으로_조회된다() {
        // when
        final ResponseEntity<LineResponse> result = lineFindByIdController.findById(1L);
        new LineResponseDto(1L, "2호선", "green", List.of(
            new InterStationResponseDto(1L, 1L, 10L, 10)
        ));
        final LineResponse expected = new LineResponse(1L, "2호선", "green", List.of(
            new InterStationResponse(1L, 1L, 10L, 10L)
        ));

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            softly.assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(expected);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastId()).isEqualTo(1L);
        });
    }
}