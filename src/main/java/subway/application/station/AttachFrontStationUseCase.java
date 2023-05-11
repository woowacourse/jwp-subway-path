package subway.application.station;

import subway.ui.dto.request.AttachFrontStationRequest;

public interface AttachFrontStationUseCase {
    void attachEndStation(Long lineId, AttachFrontStationRequest request);
}
