package subway.line.application.port;

import java.util.List;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findall.LineFindAllUseCase;

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
