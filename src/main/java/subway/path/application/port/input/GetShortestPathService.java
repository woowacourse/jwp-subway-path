package subway.path.application.port.input;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.path.domain.Path;
import subway.path.domain.ShortestPathResult;
import subway.path.dto.GetShortestPathResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class GetShortestPathService implements GetShortestPathUseCase {
    private final GetAllLinePort getAllLinePort;
    
    @Override
    public GetShortestPathResponse getShortestPath(final String startStationName, final String endStationName) {
        final Path path = new Path(getAllLinePort.getAll());
        final ShortestPathResult result = path.getShortestPath(startStationName, endStationName);
        return new GetShortestPathResponse(result);
    }
}
