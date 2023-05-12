package subway.station.application.port.input;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachEndStationUseCase {
    void attachEndStation(Long lineId, AttachStationRequest request);
}
