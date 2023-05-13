package subway.service;

import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;

public interface LineService {
    LineResponse createNewLine(LineCreateRequest request);

    LineResponse addStation(Long id, SectionCreateRequest request);
}
