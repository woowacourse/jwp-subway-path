package subway.line.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.FindAllLinesUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.SortedSection;
import subway.ui.dto.response.LineResponse;
import subway.ui.dto.response.StationResponse;

@Transactional(readOnly = true)
@Service
public class FindAllLineService implements FindAllLinesUseCase {

    private final LineRepository lineRepository;

    public FindAllLineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public List<LineResponse> findAllLines() {
        final List<Line> allLines = lineRepository.findAll();
        final List<LineResponse> lineResponses = new ArrayList<>();

        for (final Line allLine : allLines) {
            lineResponses.add(toLineResponse(allLine));
        }

        return lineResponses;
    }

    private LineResponse toLineResponse(final Line allLine) {
        final SortedSection sortedSection = new SortedSection(allLine.getSections());
        final List<StationResponse> stationResponses = toStationResponses(sortedSection);

        return new LineResponse(allLine.getLineName(), allLine.getLineColor(), stationResponses);
    }

    private List<StationResponse> toStationResponses(final SortedSection sortedSection) {
        return sortedSection.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
