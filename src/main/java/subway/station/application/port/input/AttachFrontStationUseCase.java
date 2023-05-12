package subway.station.application.port.input;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachFrontStationUseCase {
    void attachFrontStation(Long lineId, AttachStationRequest request);
}
