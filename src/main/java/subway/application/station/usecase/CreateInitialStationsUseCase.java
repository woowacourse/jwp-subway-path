package subway.application.station.usecase;

import subway.ui.dto.request.CreateInitialStationsRequest;

public interface CreateInitialStationsUseCase {
    Long addInitialStations(Long lineId, CreateInitialStationsRequest request);
}
