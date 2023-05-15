package subway.station.application.port.input;

import subway.station.dto.StationAddRequest;

public interface AddStationUseCase {
    Long addStation(final StationAddRequest request);
}
