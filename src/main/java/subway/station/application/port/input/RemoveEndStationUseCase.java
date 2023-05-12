package subway.station.application.port.input;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveEndStationUseCase {
    void removeEndStation(Long lineId, RemoveStationRequest stationId);
}
