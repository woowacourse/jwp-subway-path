package subway.line.application.port;

import java.util.List;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.create.LineCreateRequestDto;
import subway.line.application.port.in.create.LineCreateUseCase;

public class MockLineCreateUseCase implements LineCreateUseCase {

    private int callCount = 0;

    @Override
    public LineResponseDto createLine(final LineCreateRequestDto lineCreateRequestDto) {
        callCount++;
        return new LineResponseDto(
                1L,
                lineCreateRequestDto.getName(),
                lineCreateRequestDto.getColor(),
                List.of(new InterStationResponseDto(1L, 1L, 2L, 10))
        );
    }

    public int getCallCount() {
        return callCount;
    }
}
