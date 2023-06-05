package subway.ui.line.findall;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.line.application.port.MockLineFindAllUseCase;
import subway.line.ui.LineFindAllController;
import subway.line.ui.dto.in.LineResponse;
import subway.line.ui.dto.in.LinesResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 모두 조회하는 기능")
class LineFindAllControllerTest {

    private MockLineFindAllUseCase useCase;
    private LineFindAllController lineFindAllController;

    @BeforeEach
    void setUp() {
        useCase = new MockLineFindAllUseCase();
        lineFindAllController = new LineFindAllController(useCase);
    }

    @Test
    void 정상적으로_조회된다() {
        // when
        ResponseEntity<LinesResponse> allLines = lineFindAllController.findAllLines();
        LinesResponse expected = new LinesResponse(List.of(
                new LineResponse(1L, "2호선", "green", List.of()),
                new LineResponse(2L, "신분당선", "red", List.of())
        ));

        // then
        assertSoftly(softly -> {
            softly.assertThat(allLines.getStatusCode()).isEqualTo(HttpStatus.OK);
            softly.assertThat(allLines.getBody()).usingRecursiveComparison().isEqualTo(expected);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
        });
    }
}
