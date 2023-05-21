package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Path;
import subway.domain.SectionRouter;
import subway.entity.StationEntity;

import java.util.List;

@Service
public class PathService {

    private final SectionService sectionService;
    private final StationService stationService;


    public PathService(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public Path findShortestPath(Long startStationId, Long endStationId) {
        SectionRouter sectionRouter = SectionRouter.of(sectionService.findAll());
        double shortestDistance = sectionRouter.findShortestDistance(startStationId, endStationId);
        List<Long> shortestPath = sectionRouter.findShortestPath(startStationId, endStationId);

        List<StationEntity> stations = stationService.findStationsByIds(shortestPath);

        return new Path(stations, shortestDistance);
    }
}
