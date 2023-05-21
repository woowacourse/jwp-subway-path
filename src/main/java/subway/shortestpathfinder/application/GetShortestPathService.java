package subway.shortestpathfinder.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.shortestpathfinder.application.port.input.GetShortestPathUseCase;
import subway.shortestpathfinder.domain.ShortestPathFinder;
import subway.shortestpathfinder.domain.ShortestPathResult;
import subway.shortestpathfinder.dto.GetShortestPathResponse;

@Transactional(readOnly = true)
@Service
public class GetShortestPathService implements GetShortestPathUseCase {
    private final GetAllLinePort getAllLinePort;
    
    public GetShortestPathService(final GetAllLinePort getAllLinePort) {
        this.getAllLinePort = getAllLinePort;
    }
    
    @Override
    public GetShortestPathResponse getShortestPath(final String startStationName, final String endStationName) {
        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(getAllLinePort.getAll());
        final ShortestPathResult result = shortestPathFinder.getShortestPath(startStationName, endStationName);
        return new GetShortestPathResponse(result);
    }
}
