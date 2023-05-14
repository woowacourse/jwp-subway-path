package subway.application.line.port;

import java.util.List;
import subway.application.line.port.in.LineFindAllUseCase;
import subway.application.line.port.in.LineResponseDto;

public class MockLineFindAllUseCase implements LineFindAllUseCase {

    private int callCount;

    @Override
    public List<LineResponseDto> findAllLines() {
        callCount++;
        return List.of(
            new LineResponseDto(1L, "2호선", "green", List.of()),
            new LineResponseDto(2L, "신분당선", "red", List.of())
        );
    }

    public int getCallCount() {
        return callCount;
    }
}
