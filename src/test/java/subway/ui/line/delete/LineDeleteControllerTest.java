package subway.ui.line.delete;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.line.application.port.MockLineDeleteUseCase;
import subway.line.ui.LineDeleteController;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 삭제하는 기능")
class LineDeleteControllerTest {

    private MockLineDeleteUseCase useCase;
    private LineDeleteController lineDeleteController;

    @BeforeEach
    void setUp() {
        useCase = new MockLineDeleteUseCase();
        lineDeleteController = new LineDeleteController(useCase);
    }

    @Test
    void 정상적으로_삭제된다() {
        ResponseEntity<Void> result = lineDeleteController.deleteLineById(1L);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastId()).isEqualTo(1);
        });
    }
}
