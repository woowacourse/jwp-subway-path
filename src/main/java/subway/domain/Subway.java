package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

public class Subway {

    private final List<Line> lines;

    public Subway(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void add(
            final String lineName,
            final String baseStationName,
            final String additionalStationName,
            final int distanceValue,
            final Direction direction
    ) {
        final Station base = new Station(baseStationName);
        final Station additional = new Station(additionalStationName);
        final Distance distance = new Distance(distanceValue);

        validateExistLine(base, additional);

        final Line findLine = findLineByLineName(lineName);
        findLine.add(base, additional, distance, direction);
    }

    public void remove(final String lineName, final String stationName) {
        final Line findLine = findLineByLineName(lineName);
        findLine.remove(new Station(stationName));
    }

    public void initialAdd(
            final String lineName,
            final String leftStationName,
            final String rightStationName,
            final Integer distanceValue
    ) {
        if (leftStationName.equals(rightStationName)) {
            throw new InvalidSectionException("동일한 이름을 가진 역을 구간에 추가할 수 없습니다.");
        }
        final Station left = new Station(leftStationName);
        final Station right = new Station(rightStationName);
        final Distance distance = new Distance(distanceValue);

        validateExistLine(left, right);

        final Line findLine = findLineByLineName(lineName);
        findLine.initialAdd(left, right, distance);
    }

    private void validateExistLine(final Station base, final Station additional) {
        if (lines.stream().anyMatch(line -> line.containsAll(base, additional))) {
            throw new InvalidSectionException("지하철 전체 노선에 이미 존재하는 구간입니다.");
        }
    }

    public Line findLineByLineName(final String lineName) {
        return lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(LineNotFoundException::new);
    }

    public Path findShortestPath(final String startStationName, final String endStationName) {
        if (!isExistStation(startStationName) || !isExistStation(endStationName)) {
            throw new StationNotFoundException();
        }

        final WeightedMultigraph<String, DefaultWeightedEdge> graph = makeWeightGraph();
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph);
        final List<String> pathWithStationName = dijkstraShortestPath.getPath(startStationName, endStationName)
                .getVertexList();
        final double distance = dijkstraShortestPath.getPathWeight(startStationName, endStationName);
        final List<Station> stations = pathWithStationName.stream()
                .map(Station::new)
                .collect(Collectors.toList());

        return new Path(stations, new Distance((int) distance));
    }

    private Boolean isExistStation(final String stationName){
        return lines.stream()
                .anyMatch(line -> line.hasStation(stationName));
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> makeWeightGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            makeGraphEachLine(graph, line);
        }
        return graph;
    }

    private void makeGraphEachLine(final WeightedMultigraph<String, DefaultWeightedEdge> graph, final Line line) {
        final List<Section> sections = line.getSections();
        for (Section section : sections) {
            final String startName = section.getStartName();
            final String endName = section.getEndName();
            final int distance = section.getDistanceValue();

            addVertexIfNotContains(graph, startName);
            addVertexIfNotContains(graph, endName);
            graph.setEdgeWeight(graph.addEdge(startName, endName), distance);
        }
    }

    private void addVertexIfNotContains(
            final WeightedMultigraph<String, DefaultWeightedEdge> graph,
            final String stationName
    ) {
        if (!graph.containsVertex(stationName)) {
            graph.addVertex(stationName);
        }
    }

    public List<Line> getLines() {
        return lines;
    }
}
