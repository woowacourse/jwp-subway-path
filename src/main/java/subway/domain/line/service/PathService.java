package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.domain.SectionRouter;
import subway.domain.line.service.LineService;
import subway.domain.line.service.SectionService;
import subway.domain.line.domain.Path;
import subway.domain.line.entity.StationEntity;
import subway.domain.line.service.StationService;

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
