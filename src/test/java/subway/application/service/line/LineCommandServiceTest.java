package subway.application.service.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.adapter.in.web.line.dto.LineRequest;
import subway.application.port.out.line.LineCommandHandler;
import subway.application.port.out.line.LineQueryHandler;
import subway.domain.Line;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LineCommandServiceTest {
    @Mock
    private LineCommandHandler lineCommandHandler;
    @Mock
    private LineQueryHandler lineQueryHandler;
    @InjectMocks
    private LineCommandService lineCommandService;

    @Test
    @DisplayName("노선이 중복되지않으면 정상적으로 노선을 만든다.")
    void createLine() {
        given(lineQueryHandler.findByName(any()))
                .willReturn(Optional.empty());

        assertThatNoException().isThrownBy(
                () -> lineCommandService.createLine(new LineRequest("1호선", 100))
        );
    }

    @Test
    @DisplayName("존재하는 노선이면 예외처리")
    void createLineException() {
        given(lineQueryHandler.findByName(any()))
                .willReturn(Optional.of(new Line(1L, "1호선", 10)));

        assertThatThrownBy(
                () -> lineCommandService.createLine(new LineRequest("1호선", 10))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 노선이 정상적으로 삭제되는지 테스트")
    void deleteLine() {
        Long lineId = lineCommandHandler.createLine(new Line("1호선", 1));
        lineCommandService.deleteLine(lineId);

        Assertions.assertThat(lineQueryHandler.findAll()).hasSize(0);
    }
}