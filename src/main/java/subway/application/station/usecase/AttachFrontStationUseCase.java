package subway.application.station.usecase;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachFrontStationUseCase {
    void attachFrontStation(Long lineId, AttachStationRequest request);
}
