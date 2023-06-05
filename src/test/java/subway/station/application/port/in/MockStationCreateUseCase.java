package subway.station.application.port.in;

public class MockStationCreateUseCase implements StationCreateUseCase {

    private int callCount = 0;

    @Override
    public StationInfoResponseDto create(final StationCreateRequestDto requestDto) {
        callCount++;
        return new StationInfoResponseDto(1L, "강남역");
    }

    public int getCallCount() {
        return callCount;
    }
}
