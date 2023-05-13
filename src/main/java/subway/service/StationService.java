package subway.service;

import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;

public interface StationService {
    StationResponse createStation(StationCreateRequest request);
}
