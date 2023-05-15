package subway.station.application.port.input;

import subway.station.dto.StationInitAddRequest;

public interface InitAddStationUseCase {
    void initAddStations(final StationInitAddRequest request);
}
