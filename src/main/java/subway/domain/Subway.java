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

        double totalDistance = shortestRoute.getWeight();
        Charge totalCharge = chargeBooth.calculateCharge(passengerAge, shortestRoute);

        List<WeightedEdgeWithLine> edges = shortestRoute.getEdgeList();

        // a-b-e-c
        // a-b
        // b-e-c
        List<LineInPath> lines = new ArrayList<>();
        List<Station> stationsInSameLine = new ArrayList<>();

        for (int i = 0; i <= edges.size(); i++) {
            if (i==edges.size()) {
                stationsInSameLine.add(edges.get(i-1).getTarget());
                Line line = edges.get(i - 1).getLine();
                lines.add(new LineInPath(line.getId(), line.getName(), stationsInSameLine));
                continue;
            }
            if (i==0) {
                stationsInSameLine.add(edges.get(i).getSource());
            } else {
                WeightedEdgeWithLine before = edges.get(i-1);
                WeightedEdgeWithLine after = edges.get(i);
                Line beforLine = before.getLine();
                Line afterLine = after.getLine();
                if (beforLine.equals(afterLine)) {
                    // 같으면
                    stationsInSameLine.add(edges.get(i).getSource());
                } else {
                    // 다르면
                    stationsInSameLine.add(edges.get(i - 1).getTarget());
                    lines.add(new LineInPath(before.getLine().getId(), before.getLine().getName(), stationsInSameLine));
                    stationsInSameLine = new ArrayList<>();
                    stationsInSameLine.add(edges.get(i - 1).getTarget());
                }
            }
        }

        return new Path(lines, totalDistance, totalCharge);
    }
}
