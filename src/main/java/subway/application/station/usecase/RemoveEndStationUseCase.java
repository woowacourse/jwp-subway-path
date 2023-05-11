package subway.application.station.usecase;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveEndStationUseCase {
    void removeEndStation(Long lineId, RemoveStationRequest stationId);
}
