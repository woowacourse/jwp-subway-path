package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.LineRepository;
import subway.dto.PathResponse;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(final Long departureId, final Long arrivalId) {
        return new PathResponse(List.of(), 99, 99);
    }
}
