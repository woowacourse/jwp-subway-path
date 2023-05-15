package subway.application;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.section.SectionsByLine;
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

    public LineFindResponse findOrderedStationNamesByLineId(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);
        Sections sections = new Sections(sectionRepository.findAllSectionByLineId(lineId));

        return new LineFindResponse(findLine.getName(), sections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineOrderedStationNames() {
        List<Line> findLines = lineRepository.findAllLine();
        Map<Line, Sections> findAllSectionsByLine = findLines.stream()
                .collect(toMap(
                        line -> line,
                        line -> new Sections(sectionRepository.findAllSectionByLineId(line.getId())))
                );

        SectionsByLine sectionsByLine = new SectionsByLine(findAllSectionsByLine);
        Map<Line, List<String>> allSortedStationNamesByLine = sectionsByLine.getAllSortedStationNamesByLine();

        return allSortedStationNamesByLine.keySet().stream()
                .map(line -> new LineFindResponse(line.getName(), allSortedStationNamesByLine.get(line)))
                .collect(Collectors.toUnmodifiableList());
    }
}
