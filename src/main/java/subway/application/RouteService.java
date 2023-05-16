package subway.application;

import static subway.application.mapper.SectionProvider.createSections;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.application.dto.StationResponse;
import subway.application.mapper.SectionProvider;
import subway.domain.fare.Fare;
import subway.domain.fare.TotalDistance;
import subway.domain.line.LineRepository;
import subway.domain.line.LineWithSectionRes;
import subway.domain.section.Section;
import subway.domain.section.SectionDistance;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final FareCalculator fareCalculator;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public RouteService(final FareCalculator fareCalculator,
                        final LineRepository lineRepository,
                        final StationRepository stationRepository) {
        this.fareCalculator = fareCalculator;
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
        final List<Station> shortestRoute = getShortestRoute(sourceStation, targetStation, sectionsByLineId);
        final TotalDistance shortestDistance = getShortestDistance(sectionsByLineId, shortestRoute);

        final Fare normalFare = fareCalculator.calculateFare(shortestDistance);
        final Fare totalFare = normalFare.add(getMaxExtraFare(possibleSections));
        return new RouteResponse(getStationResponses(possibleSections, shortestRoute), getFareResponse(totalFare));
    }

    private List<Station> getShortestRoute(final Station sourceStation, final Station targetStation,
                                           final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph = getPossibleRouteGraph(sectionsByLineId);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            routeGraph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getPossibleRouteGraph(
        final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        sectionsByLineId.values()
            .forEach(sectionRes -> addRouteBySectionRes(routeGraph, sectionRes));
        return routeGraph;
    }

    private void addRouteBySectionRes(final WeightedMultigraph<Station, DefaultWeightedEdge> routeGraph,
                                      final List<LineWithSectionRes> sectionRes) {
        final Sections sections = createSections(sectionRes);
        final List<Station> stations = sections.getSortedStations();
        stations.forEach(routeGraph::addVertex);
        final List<Section> allSections = sections.getSections();
        addSectionsToEdge(routeGraph, allSections);
    }

    private void addSectionsToEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                   final List<Section> allSections) {
        allSections.forEach(section -> {
            final Station sourceStation = section.getSource();
            final Station targetStation = section.getTarget();
            final SectionDistance distance = section.getDistance();
            final DefaultWeightedEdge sectionEdge = graph.addEdge(sourceStation, targetStation);
            graph.setEdgeWeight(sectionEdge, distance.distance());
        });
    }

    private TotalDistance getShortestDistance(final Map<Long, List<LineWithSectionRes>> sectionsByLineId,
                                              final List<Station> shortestRoute) {
        final List<Section> allSections = getAllSections(sectionsByLineId);
        return IntStream.range(0, shortestRoute.size() - 1)
            .mapToObj(i -> new Section(shortestRoute.get(i), shortestRoute.get(i + 1), SectionDistance.zero()))
            .flatMap(section -> allSections.stream().filter(section::isSameSection))
            .map(section -> new TotalDistance(section.getDistance().distance()))
            .reduce(new TotalDistance(0), TotalDistance::add);
    }

    private Fare getMaxExtraFare(final List<LineWithSectionRes> possibleSections) {
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
                SectionProvider.getStationIdByName(station.getName(), possibleSections), station.getName().name()
            )).collect(Collectors.toUnmodifiableList());
    }

    private FareResponse getFareResponse(final Fare totalFare) {
        final Fare teenagerFare = fareCalculator.calculateTeenagerFare(totalFare);
        final Fare childFare = fareCalculator.calculateChildFare(totalFare);
        return new FareResponse(totalFare.fare(), teenagerFare.fare(), childFare.fare());
    }
}
