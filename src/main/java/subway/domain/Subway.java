package subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Subway {
    private final WeightedMultigraph<Station, WeightedEdgeWithLine> graph;

    private Subway(WeightedMultigraph<Station, WeightedEdgeWithLine> graph) {
        this.graph = graph;
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
        return new Subway(graph);
    }

    public Path findShortestRoute(int passengerAge, Station startStation, Station endStation) {
        DijkstraShortestPath<Station, WeightedEdgeWithLine> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, WeightedEdgeWithLine> shortestRoute = dijkstraShortestPath.getPath(startStation, endStation);

        double totalDistance = shortestRoute.getWeight();
        double totalCharge = calculateCharge(passengerAge, shortestRoute);

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

    private double calculateCharge(int passengerAge, GraphPath<Station, WeightedEdgeWithLine> shortestRoute) {
        double totalDistance = shortestRoute.getWeight();
        double basicFare = calculateChargeByDistance(totalDistance);

        List<WeightedEdgeWithLine> edges = shortestRoute.getEdgeList();
        double additionalCharge = calculateAdditionalChargeByLine(edges);

        return applyAgeDiscount(passengerAge, basicFare + additionalCharge);
    }

    private double calculateChargeByDistance(double totalDistance) {
        if (totalDistance <= 10) {
            return 1250;
        }
        if (totalDistance <= 50) {
            return 1250 + (int) (Math.ceil((totalDistance - 10) / 5)) * 100;
        }
        return 1250 + ((50 - 10) / 5) * 100 + (int) (Math.ceil((totalDistance - 50) / 8)) * 100;
    }

    private double calculateAdditionalChargeByLine(List<WeightedEdgeWithLine> edges) {
        return edges.stream()
                .mapToInt(it -> it.getLine().getExtraCharge())
                .distinct()
                .max()
                .orElse(0);
    }

    private double applyAgeDiscount(int passengerAge, double charge) {
        if (6 <= passengerAge && passengerAge < 13) {
            return (charge - 350) * 0.5;
        }
        if (13 <= passengerAge && passengerAge < 19) {
            return (charge - 350) * 0.8;
        }
        return charge;
    }
}
