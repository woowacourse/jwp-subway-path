package subway.station.application.port.input;

import subway.station.dto.StationSaveRequest;

public interface AddStationUseCase {
    Long addStation(final StationSaveRequest request);
}
