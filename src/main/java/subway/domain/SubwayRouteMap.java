package subway.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.InvalidSubwayPathException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubwayRouteMap {
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int PER_FIVE_KILOMETER = 5;
    private static final int ADDITIONAL_FARE = 100;
    private static final int TEN_TO_FIFTY = 40;
    private static final int PER_EIGHT_KILOMETER = 8;

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private final ShortestPathAlgorithm<String, DefaultWeightedEdge> shortestPath;
    private final Map<String, Station> stations;

    public SubwayRouteMap(final List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        insertStation(sections);
        shortestPath = new DijkstraShortestPath<>(graph);
        stations = stationByName(sections);
    }

    private void insertStation(final List<Section> sections) {
        insertVertex(sections);
        insertEdge(sections);
    }

    private void insertVertex(final List<Section> sections) {
        getStations(sections)
                .forEach(station -> graph.addVertex(station.getName()));
    }

    private List<Station> getStations(final List<Section> sections) {
        final Set<Station> uniqueStations = new HashSet<>();
        sections.forEach(section -> {
            uniqueStations.add(section.getLeft());
            uniqueStations.add(section.getRight());
        });
        return new ArrayList<>(uniqueStations);
    }

    private void insertEdge(final List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getLeft().getName(), section.getRight().getName()),
                section.getDistance().getDistance()
        ));
    }

    private Map<String, Station> stationByName(final List<Section> sections) {
        return getStations(sections).stream()
                .collect(Collectors.toMap(Station::getName, station -> station));
    }

    public int shortestDistanceBetween(final Station from, final Station to) {
        try {
            return (int) Math.round(shortestPath.getPathWeight(from.getName(), to.getName()));
        } catch (final IllegalArgumentException e) {
            throw new InvalidSubwayPathException(from.getName() + "에서 " + to.getName() + "으로 갈 수 없습니다.");
        }
    }

    public List<Station> shortestPathBetween(final Station from, final Station to) {
        try {
            final List<String> pathNames = shortestPath.getPath(from.getName(), to.getName()).getVertexList();
            return pathNames.stream()
                    .map(stations::get)
                    .collect(Collectors.toList());
        } catch (final IllegalArgumentException e) {
            throw new InvalidSubwayPathException(from.getName() + "에서 " + to.getName() + "으로 갈 수 없습니다.");
        }
    }

    public int fareBetween(final Station from, final Station to) {
        final long distance = shortestDistanceBetween(from, to);
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }
        return DEFAULT_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(final long distance) {
        return tenToFifty(distance) + overFifty(distance);
    }

    private int tenToFifty(final long distance) {
        final long overDistance = distance - DEFAULT_FARE_DISTANCE;
        if (overDistance <= 0) {
            return 0;
        }
        if (overDistance < TEN_TO_FIFTY) {
            return fareFor(overDistance, PER_FIVE_KILOMETER);
        }
        return fareFor(TEN_TO_FIFTY, PER_FIVE_KILOMETER);
    }

    private int fareFor(final long distance, final int unit) {
        final long includeEndUnitDistance = distance - 1;
        final long additionalFareCount = includeEndUnitDistance / unit;
        final long calculateFareWhenStart = additionalFareCount + 1;

        return (int) calculateFareWhenStart * ADDITIONAL_FARE;
    }

    private int overFifty(final long distance) {
        final long overDistance = distance - 50;
        if (overDistance <= 0) {
            return 0;
        }
        return fareFor(overDistance, PER_EIGHT_KILOMETER);
    }
}
