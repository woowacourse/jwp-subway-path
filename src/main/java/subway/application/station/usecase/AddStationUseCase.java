package subway.application.station.usecase;

import subway.ui.dto.request.AddStationRequest;

public interface AddStationUseCase {
    Long addStation(Long lineId, AddStationRequest request);
}
