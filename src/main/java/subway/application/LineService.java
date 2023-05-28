package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.dto.LineRequest;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long saveLine(LineRequest lineRequest) {
        Line line = new Line(null, lineRequest.getLineName());
        Line insertedLine = lineRepository.insert(line);
        return insertedLine.getId();
    }

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);
        return getLineFindResponse(findLine);
    }

    private LineFindResponse getLineFindResponse(Line findLine) {
        List<Section> findSections = sectionRepository.findSectionsByLineId(findLine.getId());
        if (findSections.isEmpty()) {
            return new LineFindResponse(findLine.getName(), new ArrayList<>());
        }
        StationConnections stationConnections = StationConnections.fromSections(findSections);
        return new LineFindResponse(findLine.getName(), stationConnections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineStationNames() {
        List<Line> lines = lineRepository.findAllLines();
        return lines.stream()
                .map(this::getLineFindResponse)
                .collect(toList());
    }

    public void updateLine(Long lineId, LineRequest updateLineRequest) {
        Line newLine = new Line(lineId, updateLineRequest.getLineName());
        lineRepository.update(newLine);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
