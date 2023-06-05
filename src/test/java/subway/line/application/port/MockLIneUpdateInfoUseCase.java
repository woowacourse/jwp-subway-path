package subway.line.application.port;

import subway.line.application.port.in.update.LineUpdateInfoUseCase;
import subway.line.application.port.in.update.LineUpdateRequestDto;

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
