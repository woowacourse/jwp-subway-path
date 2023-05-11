package subway.application.station.usecase;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveFrontStationUseCase {
    void removeFrontStation(Long lineId, RemoveStationRequest request);
}
