package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.pay.Payment;

import java.util.*;
import java.util.stream.Collectors;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, Section> graph;
    private final Map<Section, Line> sectionsLines;

    public SubwayGraph(final DijkstraShortestPath<Station, Section> graph, final Map<Section, Line> sectionsLines) {
        this.graph = graph;
        this.sectionsLines = sectionsLines;
    }

    public static SubwayGraph newInstance(final List<Line> lines) {
        return new SubwayGraph(createDijkstra(lines), createSectionsLineMap(lines));
    }

    private static DijkstraShortestPath<Station, Section> createDijkstra(final List<Line> lines) {
        final WeightedMultigraph<Station, Section> newGraph = new WeightedMultigraph<>(Section.class);

        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .forEach(section -> setUpEdge(newGraph, section));

        return new DijkstraShortestPath<>(newGraph);
    }

    private static void setUpEdge(final WeightedMultigraph<Station, Section> newGraph, final Section section) {
        newGraph.addVertex(section.getUpStation());
        newGraph.addVertex(section.getDownStation());
        newGraph.addEdge(section.getUpStation(), section.getDownStation(), section);
        newGraph.setEdgeWeight(section, section.getDistance().getValue());
    }

    private static Map<Section, Line> createSectionsLineMap(final List<Line> lines) {
        final Map<Section, Line> sectionsLines = new HashMap<>();
        lines.forEach(line -> line.getSections()
                .forEach(section -> sectionsLines.put(section, line)));

        return sectionsLines;
    }

    public Route findRouteBy(final Station from, final Station to) {
        final GraphPath<Station, Section> findPath = graph.getPath(from, to);

        // TODO: 2023/05/15 총 비용 계산
        final List<Section> findEdges = findPath.getEdgeList();

        final List<Section> sectionsInOrder = collectSectionsInOrder(from, findEdges);
        final List<Station> transferStations = collectTransferStationsInOrder(findEdges);
        final Distance totalDistance = new Distance((int) findPath.getWeight());
        final Money totalPrice = Payment.calculate(totalDistance);

        return new Route(from, to, transferStations, sectionsInOrder, totalDistance, totalPrice);
    }

    private List<Section> collectSectionsInOrder(final Station from, final List<Section> sections) {
        final Deque<Station> stations = new ArrayDeque<>(List.of(from));

        return sections.stream()
                .map(section -> reverseSection(stations, section))
                .collect(Collectors.toList());
    }

    private Section reverseSection(final Deque<Station> stations, final Section section) {
        final Station upStation = stations.pop();
        final Section sortedSection = section.sortBy(upStation);
        stations.push(sortedSection.getDownStation());

        return sortedSection;
    }

    private List<Station> collectTransferStationsInOrder(final List<Section> sections) {
        final List<Station> transferStations = new ArrayList<>();
        final Deque<Section> sectionDeque = new ArrayDeque<>(sections);

        while (sectionDeque.size() > 1) {
            final Section currentSection = sectionDeque.removeFirst();
            final Section nextSection = sectionDeque.peek();

            final Line firstSectionLine = sectionsLines.get(currentSection);
            final Line nextSectionLine = sectionsLines.get(nextSection);

            if (!firstSectionLine.equals(nextSectionLine)) {
                final Station transferStation = currentSection.findSameStationBy(nextSection);
                transferStations.add(transferStation);
            }
        }

        return transferStations;
    }
}
