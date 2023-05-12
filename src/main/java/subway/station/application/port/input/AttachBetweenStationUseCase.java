package subway.station.application.port.input;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachBetweenStationUseCase {
    void attachBetweenStation(Long lineId, AttachStationRequest request);
}
