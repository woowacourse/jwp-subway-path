package subway.application.line.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.usecase.FindSingleLineUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.path.LineRoutePath;
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
