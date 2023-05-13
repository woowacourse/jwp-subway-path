package subway.service;

import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationCreateRequest;

public interface LineService {
    LineResponse createLineWithoutStation(LineCreateRequest request);

    LineResponse addStation(Long id, StationCreateRequest request);
}
