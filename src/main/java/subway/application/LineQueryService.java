package subway.application;

import static subway.exception.line.LineExceptionType.NOT_FOUND_LINE;
import static subway.exception.station.StationExceptionType.NOT_FOUND_STATION;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineQueryResponse;
import subway.application.dto.ShortestRouteResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.LinkedRoute;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.service.ShortestRouteService;
import subway.exception.line.LineException;
import subway.exception.station.StationException;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final ShortestRouteService shortestRouteService;

    public LineQueryService(final LineRepository lineRepository,
                            final StationRepository stationRepository,
                            final ShortestRouteService shortestRouteService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.shortestRouteService = shortestRouteService;
    }

    public LineQueryResponse findById(final UUID id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineException(NOT_FOUND_LINE));
        return LineQueryResponse.from(line);
    }

    public List<LineQueryResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineQueryResponse::from)
                .collect(Collectors.toList());
    }

    public ShortestRouteResponse findShortestRoute(final String startStationName, final String endStationName) {
        final Station start = findStationByName(startStationName);
        final Station end = findStationByName(endStationName);
        final LinkedRoute result = shortestRouteService.shortestRoute(lineRepository.findAll(), start, end);
        return ShortestRouteResponse.from(result);
    }

    private Station findStationByName(final String stationName) {
        return stationRepository.findByName(stationName)
                .orElseThrow(() -> new StationException(NOT_FOUND_STATION));
    }

}
