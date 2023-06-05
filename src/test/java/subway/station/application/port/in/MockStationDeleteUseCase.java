package subway.station.application.port.in;

public class MockStationDeleteUseCase implements StationDeleteUseCase {

    private int callCount = 0;
    private long lastId;

    @Override
    public void deleteById(final long id) {
        callCount++;
        lastId = id;
    }

    public int getCallCount() {
        return callCount;
    }

    public long getLastId() {
        return lastId;
    }
}
