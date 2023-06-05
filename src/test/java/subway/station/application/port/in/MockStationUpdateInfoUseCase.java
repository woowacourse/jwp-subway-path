package subway.station.application.port.in;

public class MockStationUpdateInfoUseCase implements StationUpdateInfoUseCase {

    private int callCount = 0;
    private StationInfoUpdateRequestDto lastRequestDto;

    @Override
    public void updateStationInfo(final StationInfoUpdateRequestDto requestDto) {
        callCount++;
        lastRequestDto = requestDto;
    }

    public int getCallCount() {
        return callCount;
    }

    public StationInfoUpdateRequestDto getLastRequestDto() {
        return lastRequestDto;
    }
}
