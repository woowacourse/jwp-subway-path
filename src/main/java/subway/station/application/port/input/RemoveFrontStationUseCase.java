package subway.station.application.port.input;

import subway.ui.dto.request.RemoveStationRequest;

public interface RemoveFrontStationUseCase {
    void removeFrontStation(Long lineId, RemoveStationRequest request);
}
