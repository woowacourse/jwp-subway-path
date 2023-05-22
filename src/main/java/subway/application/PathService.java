package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.domain.Subway;
import subway.repository.LineRepository;
import subway.ui.PathResponse;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(long startStationId, long endStationId) {
        Subway subway = Subway.from(lineRepository.findAll());
        List<Station> shortestPath = subway.findShortestPath(startStationId, endStationId);
        int distance = subway.findShortestDistance(startStationId, endStationId);
        int fare = subway.findFare(startStationId, endStationId);

        return new PathResponse(shortestPath, distance, fare);
    }
}
