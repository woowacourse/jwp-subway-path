package subway.line.application.port;

import subway.line.application.port.in.delete.LineDeleteUseCase;

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
