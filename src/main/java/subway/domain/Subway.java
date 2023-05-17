package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Subway {
    private final WeightedMultigraph<Station, WeightedEdgeWithLine> graph;

    public Subway(WeightedMultigraph<Station, WeightedEdgeWithLine> graph) {
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

    public Path findShortestRoute(Station startStation, Station endStation) {
        DijkstraShortestPath<Station, WeightedEdgeWithLine> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, WeightedEdgeWithLine> shortestRoute = dijkstraShortestPath.getPath(startStation, endStation);

        List<Station> stations = shortestRoute.getVertexList();
        double totalDistance = shortestRoute.getWeight();
        int totalCharge = calculateCharge(shortestRoute);

        return new Path(stations, totalDistance, totalCharge);
    }

    private int calculateCharge(GraphPath<Station, WeightedEdgeWithLine> shortestRoute) {
        double totalDistance = shortestRoute.getWeight();
        int basicFare = calculateBasicCharge(totalDistance);

        List<WeightedEdgeWithLine> edges = shortestRoute.getEdgeList();
        int additionalCharge = calculateAdditionalChargeByLine(edges);

        int discountCharge = calulateDiscountChargeByAge();
        return basicFare + additionalCharge - discountCharge;
    }

    private int calculateBasicCharge(double totalDistance) {
        if (totalDistance <= 10) {
            return 1250;
        }
        if (totalDistance <= 50) {
            return 1250 + (int) (Math.ceil((totalDistance - 10) / 5)) * 100;
        }
        return 1250 + ((50 - 10) / 5) * 100 + (int) (Math.ceil((totalDistance - 50) / 8)) * 100;
    }

    private int calculateAdditionalChargeByLine(List<WeightedEdgeWithLine> edges) {
        return edges.stream()
                .mapToInt(it -> it.getLine().getExtraCharge())
                .distinct()
                .max()
                .orElse(0);
    }

    private static int calulateDiscountChargeByAge() {
        // TODO : step3 요구사항
        return 0;
    }
}
