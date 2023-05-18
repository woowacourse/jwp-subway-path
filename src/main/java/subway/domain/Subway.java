package subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.charge.Charge;
import subway.domain.charge.ChargeBooth;

public class Subway {
    private final WeightedMultigraph<Station, WeightedEdgeWithLine> graph;
    private final ChargeBooth chargeBooth;

    public Subway(WeightedMultigraph<Station, WeightedEdgeWithLine> graph, ChargeBooth chargeBooth) {
        this.graph = graph;
        this.chargeBooth = chargeBooth;
    }

    public static Subway create(List<Station> stations, List<Line> lines) {
        WeightedMultigraph<Station, WeightedEdgeWithLine> graph = new WeightedMultigraph(WeightedEdgeWithLine.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Line line : lines) {
            List<Section> sections = line.getSections();
            for (Section section : sections) {
                WeightedEdgeWithLine edge = new WeightedEdgeWithLine(line);
                graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
                graph.setEdgeWeight(edge, section.getDistance());
            }
        }
        return new Subway(graph, new ChargeBooth());
    }

    public Path findShortestRoute(int passengerAge, Station startStation, Station endStation) {
        DijkstraShortestPath<Station, WeightedEdgeWithLine> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, WeightedEdgeWithLine> shortestRoute = dijkstraShortestPath.getPath(startStation, endStation);

        List<Route> routes = getRoutes(shortestRoute.getEdgeList());
        double totalDistance = shortestRoute.getWeight();
        Charge totalCharge = chargeBooth.calculateCharge(passengerAge, shortestRoute);

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
