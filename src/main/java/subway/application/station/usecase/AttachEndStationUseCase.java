package subway.application.station.usecase;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachEndStationUseCase {
    void attachEndStation(Long lineId, AttachStationRequest request);
}
