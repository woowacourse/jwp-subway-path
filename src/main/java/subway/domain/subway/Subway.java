package subway.domain.subway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.vo.Distance;
import subway.domain.vo.Charge;
import subway.domain.charge.ChargeBooth;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.exception.subway.PathNotFoundException;
import subway.exception.subway.StationIsNotRegisteredOnLine;

public class Subway {
    private final WeightedMultigraph<Station, WeightedEdgeWithLine> graph;
    private final ChargeBooth chargeBooth;

    public Subway(WeightedMultigraph<Station, WeightedEdgeWithLine> graph, ChargeBooth chargeBooth) {
        this.graph = graph;
        this.chargeBooth = chargeBooth;
    }

    public static Subway create(List<Line> lines) {
        WeightedMultigraph<Station, WeightedEdgeWithLine> graph = new WeightedMultigraph<>(WeightedEdgeWithLine.class);

        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .forEach(graph::addVertex);

        for (Line line : lines) {
            List<Section> sections = line.getSections();
            for (Section section : sections) {
                WeightedEdgeWithLine edge = new WeightedEdgeWithLine(line);
                graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                graph.setEdgeWeight(edge, section.getDistance().getValue());
            }
        }
        return new Subway(graph, new ChargeBooth());
    }

    public Path findShortestRoute(int passengerAge, Station startStation, Station endStation) {
        if (!graph.containsVertex(startStation) || !graph.containsVertex(endStation)) {
            throw new StationIsNotRegisteredOnLine();
        }

        DijkstraShortestPath<Station, WeightedEdgeWithLine> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, WeightedEdgeWithLine> shortestRoute = dijkstraShortestPath.getPath(startStation, endStation);

        if (shortestRoute == null) {
            throw new PathNotFoundException();
        }

        Distance totalDistance = new Distance(shortestRoute.getWeight());
        List<Line> linesInRoute = shortestRoute.getEdgeList().stream()
                .map(WeightedEdgeWithLine::getLine)
                .collect(Collectors.toList());
        Charge totalCharge = chargeBooth.calculateCharge(passengerAge, totalDistance, linesInRoute);
        List<Route> routes = getRoutes(shortestRoute.getEdgeList());
        return new Path(routes, totalDistance, totalCharge);
    }

    private List<Route> getRoutes(List<WeightedEdgeWithLine> edges) {
        List<Route> lines = new ArrayList<>();
        List<Station> stationsInSameLine = new ArrayList<>();

        for (WeightedEdgeWithLine edge : edges) {
            Station currentSource = edge.getSource();
            Station currentTarget = edge.getTarget();
            if (stationsInSameLine.isEmpty()) {
                stationsInSameLine.add(currentSource);
            }
            stationsInSameLine.add(currentTarget);
            if (!hasSameLineAsNextEdge(edge, edges)) {
                Line currentLine = edge.getLine();
                lines.add(new Route(currentLine.getId(), currentLine.getName(), new ArrayList<>(stationsInSameLine)));
                stationsInSameLine.clear();
            }
        }
        return lines;
    }

    private boolean hasSameLineAsNextEdge(WeightedEdgeWithLine currentEdge, List<WeightedEdgeWithLine> edges) {
        int currentIndex = edges.indexOf(currentEdge);
        if (currentIndex < edges.size() - 1) {
            WeightedEdgeWithLine nextEdge = edges.get(currentIndex + 1);
            return currentEdge.getLine().equals(nextEdge.getLine());
        }
        return false;
    }
}
