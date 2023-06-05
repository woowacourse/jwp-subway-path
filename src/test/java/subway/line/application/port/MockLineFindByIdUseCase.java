package subway.line.application.port;

import java.util.List;
import subway.line.application.port.in.InterStationResponseDto;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findById.LineFindByIdUseCase;

public class MockLineFindByIdUseCase implements LineFindByIdUseCase {

    private int callCount;
    private long lastId;

    @Override
    public LineResponseDto findById(final Long id) {
        callCount++;
        lastId = id;
        return new LineResponseDto(1L, "2호선", "green", List.of(
                new InterStationResponseDto(1L, 1L, 10L, 10)
        ));
    }

    public int getCallCount() {
        return callCount;
    }

    public long getLastId() {
        return lastId;
    }
}
