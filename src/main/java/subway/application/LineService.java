package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);

        Sections sections = new Sections(sectionRepository.findAllSectionByLineId(lineId));
        StationConnections stationConnections = new StationConnections(sections.generateStationConnections());

        return new LineFindResponse(findLine.getName(), stationConnections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineStationNames() {
        List<Line> findLines = lineRepository.findAllLine();
        return findLines.stream()
                .map(line -> findStationNamesByLineId(line.getId()))
                .collect(toList());
    }
}
