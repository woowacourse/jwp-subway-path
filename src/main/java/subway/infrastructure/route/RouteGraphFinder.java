package subway.infrastructure.route;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Payment;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.route.Route;
import subway.domain.route.RouteEdge;
import subway.domain.route.RouteFinder;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Component
public class RouteGraphFinder implements RouteFinder {

    @Override
    public Route findRouteBy(final List<Line> lines, final Station start, final Station end) {
        final DijkstraShortestPath<Station, RouteEdge> graph = createDijkstra(lines);
        return findRouteBy(graph, start, end);
    }

    private DijkstraShortestPath<Station, RouteEdge> createDijkstra(final List<Line> lines) {
        final WeightedMultigraph<Station, RouteEdge> newGraph = new WeightedMultigraph<>(RouteEdge.class);
        lines.forEach(line -> line.getSections()
                .forEach(section -> setUpEdge(newGraph, RouteEdge.from(section, line.getNameValue()))));
        return new DijkstraShortestPath<>(newGraph);
    }

    private void setUpEdge(final WeightedMultigraph<Station, RouteEdge> newGraph, final RouteEdge section) {
        newGraph.addVertex(section.getUpStation());
        newGraph.addVertex(section.getDownStation());
        newGraph.addEdge(section.getUpStation(), section.getDownStation(), section);
        newGraph.addEdge(section.getDownStation(), section.getUpStation(), section);
        newGraph.setEdgeWeight(section, section.getDistance().getValue());
    }

    public Route findRouteBy(final DijkstraShortestPath<Station, RouteEdge> graph, final Station start, final Station end) {
        final GraphPath<Station, RouteEdge> findPath = graph.getPath(start, end);
        final List<RouteEdge> findSections = findPath.getEdgeList();
        final List<Station> findStations = findPath.getVertexList();

        final List<RouteEdge> sectionsInOrder = collectSectionsInOrder(findSections, findStations);
        final List<Station> transfersInOrder = collectTransfersInOrder(findSections, findStations);
        final Distance totalDistance = Distance.from((int) findPath.getWeight());
        final Money totalPrice = Payment.calculate(totalDistance);

        return new Route(start, end, transfersInOrder, sectionsInOrder, totalDistance, totalPrice);
    }

    private List<Station> collectTransfersInOrder(final List<RouteEdge> sections, final List<Station> stations) {
        final List<Station> transfersInOrder = new ArrayList<>();
        final Deque<RouteEdge> sectionDeque = new ArrayDeque<>(sections);
        final Deque<Station> stationDeque = new ArrayDeque<>(stations);
        stationDeque.pollFirst();
        stationDeque.pollLast();

        while (stationDeque.size() >= 1) {
            final RouteEdge currentSection = sectionDeque.pollFirst();
            final RouteEdge nextSection = sectionDeque.peek();
            final Station transfer = stationDeque.pollFirst();
            if (!currentSection.getLineName().equals(nextSection.getLineName())) {
                transfersInOrder.add(transfer);
            }
        }

        return transfersInOrder;
    }

    private List<RouteEdge> collectSectionsInOrder(final List<RouteEdge> sections, final List<Station> stations) {
        final List<RouteEdge> sectionsInOrder = new ArrayList<>();
        final Deque<RouteEdge> sectionDeque = new ArrayDeque<>(sections);
        final Deque<Station> stationDeque = new ArrayDeque<>(stations);

        while (stationDeque.size() != 1) {
            final Station leftStation = stationDeque.removeFirst();
            final Station rightStation = stationDeque.peek();
            final RouteEdge currentSection = sectionDeque.removeFirst();
            final Section sectionInOrder = new Section(currentSection.getDistance(), false, leftStation, rightStation);
            sectionsInOrder.add(RouteEdge.from(
                    sectionInOrder, currentSection.getLineName().getValue()));
        }

        return sectionsInOrder;
    }
}
