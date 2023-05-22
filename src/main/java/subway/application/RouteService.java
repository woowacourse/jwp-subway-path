package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.DiscountPolicy;
import subway.domain.FarePolicy;
import subway.domain.Route;
import subway.domain.RouteGraph;
import subway.domain.Section;
import subway.dto.RouteRequest;
import subway.dto.RouteResponse;
import subway.dto.StationResponse;

@Service
public class RouteService {
    
    private final StationService stationService;
    private final SectionService sectionService;
    
    public RouteService(final StationService stationService,
            final SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }
    
    @Transactional(readOnly = true)
    public RouteResponse findRouteBetween(final RouteRequest routeRequest) {
        final List<Section> sections = this.sectionService.findAll()
                .stream()
                .map(Section::from)
                .collect(Collectors.toUnmodifiableList());
        final RouteGraph routeGraph = RouteGraph.from(sections, routeRequest.getSourceStationId(),
                routeRequest.getTargetStationId());
        final Route route = routeGraph.getRoute();
        final List<StationResponse> stationResponses = route.getStationIds()
                .stream()
                .map(this.stationService::findStationResponseById)
                .collect(Collectors.toUnmodifiableList());
        return RouteResponse.of(stationResponses, route.getDistance(), route.calculateFare(new FarePolicy(
                DiscountPolicy.NO_DISCOUNT)));
    }
}
