package subway.application.line.port;

import subway.application.line.port.in.LineDeleteUseCase;

public class MockLineDeleteUseCase implements LineDeleteUseCase {

    private int callCount;
    private int lastId;

    @Override
    public void deleteLineById(final Long id) {
        callCount++;
        lastId = id.intValue();
    }

    public int getCallCount() {
        return callCount;
    }

    public int getLastId() {
        return lastId;
    }
}
