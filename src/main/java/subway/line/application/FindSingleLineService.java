package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.FindSingleLineUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Sections;
import subway.section.domain.SortedSection;
import subway.ui.dto.response.LineResponse;
import subway.ui.dto.response.StationResponse;

@Transactional(readOnly = true)
@Service
public class FindSingleLineService implements FindSingleLineUseCase {

    private final LineRepository lineRepository;

    public FindSingleLineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public LineResponse findSingleLine(final Long id) {
        final Line line = lineRepository.findById(id);

        final Sections sections = line.getSections();
        final SortedSection sortedSection = new SortedSection(sections);

        final List<StationResponse> stationResponses = toStationResponses(sortedSection);
        return new LineResponse(line.getLineName(), line.getLineColor(), stationResponses);
    }

    private List<StationResponse> toStationResponses(final SortedSection sortedSection) {
        return sortedSection.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
