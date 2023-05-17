package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Subway {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private final List<Line> lines;

    public Subway(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Line> lines) {
        this.graph = graph;
        this.lines = lines;
    }

    public static Subway create(List<Station> stations, List<Line> lines) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station.getName());
        }
        for (Line line : lines) {
            List<Section> sections = line.getSections();
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()),
                        section.getDistance());
            }
        }
        return new Subway(graph, lines);
    }

    public ShortestRoute findShortestRoute(Station startStation, Station endStation) {
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> shortestRoute = dijkstraShortestPath.getPath(startStation.getName(),
                endStation.getName());
        List<String> stationsInRoute = shortestRoute.getVertexList();
        double totalDistance = shortestRoute.getWeight();

        return new ShortestRoute(stationsInRoute, totalDistance);
    }

    public int calculateFare(ShortestRoute shortestRoute) {
        // basic 요금 계산
        int basicFare = calculateBasicFare(shortestRoute.getTotalDistance());
        // 노션별 추가 요금 반영
        int additionalCharge = calculateAdditionalChargeByLine();
        // 연령별 요금 할인 반영
        int discountCharge = calulateDiscountChargeByAge();
        return basicFare + additionalCharge - discountCharge;
    }

    private int calculateBasicFare(double totalDistance) {
        if (totalDistance <= 10) {
            return 1250;
        }
        if (totalDistance <= 50) {
            return 1250 + (int) (Math.ceil((totalDistance - 10) / 5)) * 100;
        }
        return 1250 + ((50 - 10) / 5) * 100 + (int) (Math.ceil((totalDistance - 50) / 8)) * 100;
    }

    private int calculateAdditionalChargeByLine() {
        // TODO : step3 요구사항
        return 0;
    }

    private int calulateDiscountChargeByAge() {
        // TODO : step3 요구사항
        return 0;
    }
}
