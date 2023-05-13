package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.LineMap;
import subway.domain.subway.Sections;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.repository.SectionRepository;

import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;

    public SubwayMapService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse showLineMap(final long lineNumber) {
        Sections sections = sectionRepository.findSectionsByLineNumber(lineNumber);
        LineMap lineMap = new LineMap(sections);

        return lineMap.getOrderedStations(sections).stream()
                .map(StationResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), LineMapResponse::from));
    }
}
