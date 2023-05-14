package subway.application.line.port;

import subway.application.line.port.in.LineUpdateInfoUseCase;
import subway.application.line.port.in.LineUpdateRequestDto;

public class MockLIneUpdateInfoUseCase implements LineUpdateInfoUseCase {

    private int callCount = 0;
    private LineUpdateRequestDto lastRequestDto;

    @Override
    public void updateLine(final LineUpdateRequestDto lineUpdateRequestDto) {
        callCount++;
        lastRequestDto = lineUpdateRequestDto;
    }

    public int getCallCount() {
        return callCount;
    }

    public LineUpdateRequestDto getLastRequestDto() {
        return lastRequestDto;
    }
}
