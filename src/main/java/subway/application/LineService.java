package subway.application;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.general.GeneralSections;
import subway.domain.section.general.GeneralSectionsByLine;
import subway.dto.LineFindResponse;
import subway.repository.GeneralSectionRepository;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final GeneralSectionRepository generalSectionRepository;

    public LineService(final LineRepository lineRepository, final GeneralSectionRepository generalSectionRepository) {
        this.lineRepository = lineRepository;
        this.generalSectionRepository = generalSectionRepository;
    }

    public LineFindResponse findOrderedStationNamesByLineId(Long lineId) {
        Line findLine = lineRepository.findLineById(lineId);
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(lineId));

        return new LineFindResponse(findLine.getName(), generalSections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineOrderedStationNames() {
        List<Line> findLines = lineRepository.findAllLine();
        Map<Line, GeneralSections> findAllSectionsByLine = findLines.stream()
                .collect(toMap(
                        line -> line,
                        line -> new GeneralSections(generalSectionRepository.findAllSectionByLineId(line.getId())))
                );

        GeneralSectionsByLine generalSectionsByLine = new GeneralSectionsByLine(findAllSectionsByLine);
        Map<Line, List<String>> allSortedStationNamesByLine = generalSectionsByLine.getAllSortedStationNamesByLine();

        return allSortedStationNamesByLine.keySet().stream()
                .map(line -> new LineFindResponse(line.getName(), allSortedStationNamesByLine.get(line)))
                .collect(Collectors.toUnmodifiableList());
    }
}
