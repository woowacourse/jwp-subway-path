package subway.station.application.port.in;

public class MockStationFindByIdUseCase implements StationFindByIdUseCase {

    private int callCount = 0;
    private long lastId;

    @Override
    public StationInfoResponseDto findStationInfoById(final long id) {
        callCount++;
        lastId = id;
        return new StationInfoResponseDto(id, "name");
    }

    public int getCallCount() {
        return callCount;
    }

    public long getLastId() {
        return lastId;
    }
}
