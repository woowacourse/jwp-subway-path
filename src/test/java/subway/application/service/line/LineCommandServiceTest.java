package subway.application.service.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.application.port.out.line.LineCommandPort;
import subway.application.port.out.line.LineQueryPort;
import subway.domain.Line;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class LineCommandServiceTest {
    private LineCommandPort lineCommandPort;
    private LineQueryPort lineQueryPort;
    private LineCommandService lineCommandService;

    @BeforeEach
    void setUp() {
        lineCommandPort = Mockito.mock(LineCommandPort.class);
        lineQueryPort = Mockito.mock(LineQueryPort.class);
        lineCommandService = new LineCommandService(lineCommandPort, lineQueryPort);
    }

    @Test
    @DisplayName("노선이 중복되지않으면 정상적으로 노선을 만든다.")
    void createLine() {
        given(lineQueryPort.findByName(any()))
                .willReturn(Optional.empty());

        assertThatNoException().isThrownBy(
                () -> lineCommandService.createLine(new LineRequest("1호선"))
        );
    }

    @Test
    @DisplayName("존재하는 노선이면 예외처리")
    void createLineException() {
        given(lineQueryPort.findByName(any()))
                .willReturn(Optional.of(new Line(1L, "1호선")));

        assertThatThrownBy(
                () -> lineCommandService.createLine(new LineRequest("1호선"))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 노선이 정상적으로 삭제되는지 테스트")
    void deleteLine() {
        Long lineId = lineCommandPort.createLine(new Line("1호선"));
        lineCommandService.deleteLine(lineId);

        Assertions.assertThat(lineQueryPort.findAll()).hasSize(0);
    }
}