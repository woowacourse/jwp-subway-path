package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Path;
import subway.domain.Subway;
import subway.dto.ShortestPathResponse;
import subway.repository.LineRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final String startStationName, final String endStationName) {
        Subway subway = new Subway(lineRepository.findAll());
        Path shortestPath = subway.findShortestPath(startStationName, endStationName);
        return ShortestPathResponse.from(shortestPath);
    }
}
