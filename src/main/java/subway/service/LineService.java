package subway.service;

import org.springframework.transaction.annotation.Transactional;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;

import java.util.List;

public interface LineService {

    @Transactional
    List<LineResponse> findAll();

    @Transactional
    LineResponse createNewLine(LineCreateRequest request);

    @Transactional
    LineResponse createInitialSection(Long id, InitialSectionCreateRequest request);

    @Transactional
    LineResponse addStation(Long id, SectionCreateRequest request);

    @Transactional
    void deleteStation(Long lineId, Long stationId);
}
