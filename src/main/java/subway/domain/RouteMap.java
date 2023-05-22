package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class RouteMap {

    public static final int BASIC_FARE = 1250;
    public static final int NON_ADDITIONAL_CHARGE = 0;
    public static final int FIRST_ADDITIONAL_CHARGE_DISTANCE_STANDARD = 10;
    public static final int SECOND_ADDITIONAL_CHARGE_DISTANCE_STANDARD = 50;
    public static final int ADDITIONAL_FARE_UNIT = 100;
    public static final int EVERY_KILO_FOR_1ST_ADDITIONAL_CHARGE = 5;
    public static final int EVERY_KILO_FOR_2ND_ADDITIONAL_CHARGE = 8;
    public static final int MAX_DISTANCE_FOR_1ST_ADDITIONAL_CHARGE = 40;

    public final WeightedMultigraph<String, DefaultWeightedEdge> graph;

    private RouteMap(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static RouteMap generateRouteMap(List<Line> lines) {
        return new RouteMap(makeGraph(lines));
    }

    public static WeightedMultigraph<String, DefaultWeightedEdge> makeGraph(List<Line> lines) {

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertex(graph, line);
        }

        for (Line line : lines) {
            addEdge(graph, line);
        }

        return graph;
    }

    private static void addEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getStations().getCurrent().getName(), section.getStations().getNext().getName()), section.getStations().getDistance());
        }
    }

    private static void addVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            if (!graph.containsVertex(section.getStations().getCurrent().getName())) {
                graph.addVertex(section.getStations().getCurrent().getName());
            }
            if (!graph.containsVertex(section.getStations().getNext().getName())) {
                graph.addVertex(section.getStations().getNext().getName());
            }
        }
    }

    public List<String> findShortestPath(String startStation, String endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);

        List<String> path = shortestPath.getPath(startStation, endStation).getVertexList();

        return path;
    }

    public double findShortestDistance(String startStation, String endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);

        double shortestDistance = shortestPath.getPathWeight(startStation, endStation);

        return shortestDistance;
    }

    public int calculateFare(double distance) {
        return BASIC_FARE + get1stAdditionalFare(distance) + get2ndAdditionalFare(distance);
    }

    private Integer get1stAdditionalFare(double distance) {
        if (distance > FIRST_ADDITIONAL_CHARGE_DISTANCE_STANDARD) {
            double additionalDistance = Math.min((distance - FIRST_ADDITIONAL_CHARGE_DISTANCE_STANDARD), MAX_DISTANCE_FOR_1ST_ADDITIONAL_CHARGE);
            int additionalFare = (int) ((Math.floor((additionalDistance - 1) / EVERY_KILO_FOR_1ST_ADDITIONAL_CHARGE) + 1) * ADDITIONAL_FARE_UNIT);
            return additionalFare;
        }
        return NON_ADDITIONAL_CHARGE;
    }

    private Integer get2ndAdditionalFare(double distance) {
        if (distance > SECOND_ADDITIONAL_CHARGE_DISTANCE_STANDARD) {
            double additionalDistance = distance - SECOND_ADDITIONAL_CHARGE_DISTANCE_STANDARD;
            int additionalFare = (int) ((Math.floor((additionalDistance - 1) / EVERY_KILO_FOR_2ND_ADDITIONAL_CHARGE) + 1) * ADDITIONAL_FARE_UNIT);
            return additionalFare;
        }
        return NON_ADDITIONAL_CHARGE;
    }

    public WeightedMultigraph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
