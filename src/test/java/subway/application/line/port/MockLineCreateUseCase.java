package subway.application.line.port;

import java.util.List;
import subway.application.line.port.in.InterStationResponseDto;
import subway.application.line.port.in.LineCreateRequestDto;
import subway.application.line.port.in.LineCreateResponseDto;
import subway.application.line.port.in.LineCreateUseCase;

public class MockLineCreateUseCase implements LineCreateUseCase {

    private int callCount = 0;

    @Override
    public LineCreateResponseDto createLine(final LineCreateRequestDto lineCreateRequestDto) {
        callCount++;
        return new LineCreateResponseDto(
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
