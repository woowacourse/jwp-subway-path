package subway.application.station.usecase;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachBetweenStationUseCase {
    void attachBetweenStation(Long lineId, AttachStationRequest request);
}
