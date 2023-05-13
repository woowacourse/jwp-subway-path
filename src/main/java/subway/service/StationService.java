package subway.service;

import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;

public interface StationService {

    @Transactional
    StationResponse createStation(StationCreateRequest request);
}
