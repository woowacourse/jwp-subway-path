package subway.ui.line.update;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.application.port.MockLIneUpdateInfoUseCase;
import subway.line.ui.LineUpdateInfoController;
import subway.line.ui.dto.in.LineUpdateInfoRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("지하철 노선을 수정하는 기능")
class LineUpdateControllerTest {

    private MockLIneUpdateInfoUseCase useCase;
    private LineUpdateInfoController lineUpdateController;

    @BeforeEach
    void setUp() {
        useCase = new MockLIneUpdateInfoUseCase();
        lineUpdateController = new LineUpdateInfoController(useCase);
    }

    @Test
    void 정상적으로_수정된다() {
        // when
        lineUpdateController.updateLineInfo(1L, new LineUpdateInfoRequest("2호선", "초록색"));

        // then
        assertSoftly(softly -> {
            softly.assertThat(useCase.getCallCount()).isEqualTo(1);
            softly.assertThat(useCase.getLastRequestDto().getName()).isEqualTo("2호선");
            softly.assertThat(useCase.getLastRequestDto().getColor()).isEqualTo("초록색");
        });
    }
}
