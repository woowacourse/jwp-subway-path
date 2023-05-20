package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import subway.cache.RouteCache;
import subway.domain.subway.Line;
import subway.domain.subway.LineMap;
import subway.domain.subway.Lines;
import subway.domain.subway.Route;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.route.ShortestPathRequest;
import subway.dto.route.ShortestPathResponse;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.event.RouteUpdateEvent;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public SubwayMapService(final SectionRepository sectionRepository, final LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse showLineMapByLineNumber(final Long lineNumber) {
        Sections sections = sectionRepository.findSectionsByLineNumber(lineNumber);
        LineMap lineMap = LineMap.from(sections);

        return lineMap.getOrderedStations(sections).stream()
                .map(StationResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), LineMapResponse::from));
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final ShortestPathRequest req) {
        Route route = RouteCache.getRoute();

        Map<Station, Set<String>> StationWithLinesMap = route.findShortestRoute(req.getStart(), req.getDestination());

        return ShortestPathResponse.from(StationWithLinesMap, route.getFee());
    }

    @TransactionalEventListener
    public void updateRoute(final RouteUpdateEvent event) {
        List<Line> lines = lineRepository.findAll().stream()
                .map(lineEntity -> {
                    Sections sections = sectionRepository.findSectionsByLineNumber(lineEntity.getLineNumber());
                    return lineRepository.findByLineNameAndSections(lineEntity.getName(), sections);
                })
                .collect(Collectors.toList());

        RouteCache.update(new Lines(lines));
    }
}
