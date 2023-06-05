package subway.station.application.port.in;

import java.util.List;

public class MockStationFindAllUseCase implements StationFindAllUseCase {

    private int callCount = 0;

    @Override
    public List<StationInfoResponseDto> findAll() {
        callCount++;
        return List.of(new StationInfoResponseDto(1L, "name1"),
            new StationInfoResponseDto(2L, "name2"));
    }

    public int getCallCount() {
        return callCount;
    }
}
