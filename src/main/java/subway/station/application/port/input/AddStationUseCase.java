package subway.station.application.port.input;

import subway.station.dto.AddStationRequest;

public interface AddStationUseCase {
    Long addStation(final AddStationRequest request);
}
