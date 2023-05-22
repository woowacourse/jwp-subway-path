package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.SubwayRouteMap;
import subway.dto.PathResponse;
import subway.dto.StationResponse;

@Service
public class SubwayService {
    private final SectionService sectionService;
    private final StationService stationService;

    public SubwayService(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBetween(final long fromId, final long toId) {
        final SubwayRouteMap subwayRouteMap = new SubwayRouteMap(sectionService.findAll());
        final Station from = stationService.findById(fromId);
        final Station to = stationService.findById(toId);
        return new PathResponse(
                StationResponse.of(subwayRouteMap.shortestPathBetween(from, to)),
                subwayRouteMap.fareBetween(from, to),
                subwayRouteMap.shortestDistanceBetween(from, to)
        );
    }
}
