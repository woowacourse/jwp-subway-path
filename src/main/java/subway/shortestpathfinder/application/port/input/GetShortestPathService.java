package subway.shortestpathfinder.application.port.input;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.shortestpathfinder.domain.ShortestPathFinder;
import subway.shortestpathfinder.domain.ShortestPathResult;
import subway.shortestpathfinder.dto.GetShortestPathResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class GetShortestPathService implements GetShortestPathUseCase {
    private final GetAllLinePort getAllLinePort;
    
    @Override
    public GetShortestPathResponse getShortestPath(final String startStationName, final String endStationName) {
        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(getAllLinePort.getAll());
        final ShortestPathResult result = shortestPathFinder.getShortestPath(startStationName, endStationName);
        return new GetShortestPathResponse(result);
    }
}
