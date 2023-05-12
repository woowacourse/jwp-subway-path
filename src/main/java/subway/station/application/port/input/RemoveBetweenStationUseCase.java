package subway.station.application.port.input;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveBetweenStationUseCase {
    void removeBetweenStation(Long lineId, RemoveStationRequest request);
}
