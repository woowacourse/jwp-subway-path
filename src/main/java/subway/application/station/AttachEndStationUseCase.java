package subway.application.station;

import subway.ui.dto.request.AttachStationRequest;

public interface AttachEndStationUseCase {
    void attachEndStation(Long lineId, AttachStationRequest request);
}
