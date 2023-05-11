package subway.application.station;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachBetweenStationUseCase {
    void attachBetweenStation(Long lineId, AttachStationRequest request);
}
