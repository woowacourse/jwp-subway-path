package subway.application.line.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.usecase.FindAllLinesUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.path.LineRoutePath;
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

        for (final Line line : allLines) {
            lineResponses.add(toLineResponse(line));
        }

        return lineResponses;
    }

    private LineResponse toLineResponse(final Line line) {
        final LineRoutePath lineRoutePath = new LineRoutePath(line);
        final List<StationResponse> stationResponses = toStationResponses(lineRoutePath);

        return new LineResponse(line.getLineName(), line.getLineColor(), stationResponses);
    }

    private List<StationResponse> toStationResponses(final LineRoutePath lineRoutePath) {
        return lineRoutePath.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
