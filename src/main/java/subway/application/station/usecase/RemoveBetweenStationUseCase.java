package subway.application.station.usecase;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveBetweenStationUseCase {
    void removeBetweenStation(Long lineId, RemoveStationRequest request);
}
