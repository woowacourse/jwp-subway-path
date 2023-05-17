package subway.application.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.ui.dto.request.LineRequest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CreateLineServiceTest {

    private LineRepository lineRepository;
    private CreateLineService createLineService;

    @BeforeEach
    void setUp() {
        lineRepository = Mockito.mock(LineRepository.class);
        createLineService = new CreateLineService(lineRepository);
    }

    @Test
    @DisplayName("노선을 정상적으로 만든다.")
    void createLine() {
        given(lineRepository.findByName(any()))
                .willReturn(Optional.empty());

        assertThatNoException().isThrownBy(
                () -> createLineService.createLine(new LineRequest("1호선"))
        );
    }

    @Test
    @DisplayName("존재하는 노선이면 예외처리")
    void createLineException() {
        given(lineRepository.findByName(any()))
                .willReturn(Optional.of(new Line(1L, "1호선")));

        assertThatThrownBy(
                () -> createLineService.createLine(new LineRequest("1호선"))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}