package subway.path.facade;

import org.springframework.stereotype.Component;
import subway.path.presentation.dto.response.PathResponse;
import subway.path.service.PathService;

@Component
public class PathFacade {

    private final PathService pathService;

    public PathFacade(final PathService pathService) {
        this.pathService = pathService;
    }

    public PathResponse getByDijkstra(final Long upStationId, final Long downStationId) {
        return pathService.findByDijkstra(upStationId, downStationId);
    }

}
