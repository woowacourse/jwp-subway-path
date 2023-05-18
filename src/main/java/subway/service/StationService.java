package subway.service;

import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;

public interface StationService {

    @Transactional(readOnly = true)
    StationResponse createStation(StationCreateRequest request);
}
