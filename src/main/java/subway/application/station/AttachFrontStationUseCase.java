package subway.application.station;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachFrontStationUseCase {
    void attachFrontStation(Long lineId, AttachStationRequest request);
}
