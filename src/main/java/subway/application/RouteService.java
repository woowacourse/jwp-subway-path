package subway.application;

import static subway.application.mapper.SectionProvider.createSections;
import static subway.application.mapper.SectionProvider.getStationIdByName;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.application.dto.StationResponse;
import subway.application.mapper.SectionProvider;
import subway.domain.fare.DiscountFare;
import subway.domain.fare.Fare;
import subway.domain.fare.TotalFare;
import subway.domain.line.LineRepository;
import subway.domain.line.LineWithSectionRes;
import subway.domain.route.Distance;
import subway.domain.route.RouteGraph;
import subway.domain.route.ShortestRoute;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public RouteService(final LineRepository lineRepository,
                        final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public RouteResponse getShortestRouteAndFare(final Long sourceStationId, final Long targetStationId) {
        final Station sourceStation = stationRepository.findById(sourceStationId);
        final Station targetStation = stationRepository.findById(targetStationId);
        final List<LineWithSectionRes> possibleSections = lineRepository.getPossibleSections(
            sourceStationId, targetStationId);

        final Map<Long, List<LineWithSectionRes>> sectionsByLineId = possibleSections.stream()
            .collect(Collectors.groupingBy(LineWithSectionRes::getLineId));

        final ShortestRoute shortestRoute = getShortestRoute(sourceStation, targetStation, sectionsByLineId);
        final List<Section> sections = getAllSections(sectionsByLineId);

        final Distance shortestDistance = shortestRoute.getShortestDistance(sections);
        final Fare lineExtraFare = getMaxLineExtraFare(possibleSections);
        final TotalFare totalFare = new TotalFare(shortestDistance, lineExtraFare);
        return new RouteResponse(getStationResponses(possibleSections, shortestRoute.getShortestRoutes()),
            getFareResponse(totalFare.totalFare()));
    }

    private ShortestRoute getShortestRoute(final Station sourceStation, final Station targetStation,
                                           final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        final RouteGraph routeGraph = new RouteGraph();
        sectionsByLineId.values().forEach(sectionRes -> {
            final Sections sections = createSections(sectionRes);
            routeGraph.addPossibleRoute(sections);
        });
        routeGraph.validateRequestRoute(sourceStation, targetStation);
        return new ShortestRoute(routeGraph, sourceStation, targetStation);
    }

    private Fare getMaxLineExtraFare(final List<LineWithSectionRes> possibleSections) {
        final int maxFare = possibleSections.stream()
            .mapToInt(LineWithSectionRes::getExtraFare)
            .max().orElse(0);
        return new Fare(maxFare);
    }

    private List<Section> getAllSections(final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        return sectionsByLineId.values().stream()
            .map(SectionProvider::createSections)
            .flatMap(sections -> sections.getSections().stream())
            .collect(Collectors.toList());
    }

    private List<StationResponse> getStationResponses(final List<LineWithSectionRes> possibleSections,
                                                      final List<Station> shortestRoute) {
        return shortestRoute.stream()
            .map(station -> new StationResponse(
                getStationIdByName(station.getName(), possibleSections), station.getName().name()))
            .collect(Collectors.toUnmodifiableList());
    }

    private FareResponse getFareResponse(final Fare totalFare) {
        final DiscountFare discountFare = new DiscountFare(totalFare);
        return new FareResponse(totalFare.fare(), discountFare.calculateTeenagerFare().fare(),
            discountFare.calculateChildFare().fare());
    }
}
