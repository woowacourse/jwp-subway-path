package subway.station.application.port.input;

import subway.station.dto.StationInitSaveRequest;

public interface InitAddStationUseCase {
    void initAddStations(final StationInitSaveRequest request);
}
