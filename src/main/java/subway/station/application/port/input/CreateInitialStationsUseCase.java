package subway.station.application.port.input;

import subway.ui.dto.request.CreateInitialStationsRequest;

public interface CreateInitialStationsUseCase {
    Long addInitialStations(Long lineId, CreateInitialStationsRequest request);
}
