package subway.station.application.port.input;

import subway.station.dto.InitAddStationRequest;

public interface InitAddStationUseCase {
    void initAddStations(final InitAddStationRequest request);
}
