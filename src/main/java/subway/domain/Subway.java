package subway.domain;

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

        List<Station> stations = shortestRoute.getVertexList();
        double totalDistance = shortestRoute.getWeight();
        double totalCharge = calculateCharge(passengerAge, shortestRoute);

        return new Path(stations, totalDistance, totalCharge);
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
