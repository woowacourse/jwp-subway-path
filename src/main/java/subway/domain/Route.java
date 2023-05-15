package subway.domain;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Route {

    private final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraGraph;

    public Route(final List<Line> lines) {
        Graph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (final Line line : lines) {
            final List<Section> sections = line.getSections();
            makeGraphFromSections(graph, sections);
        }

        dijkstraGraph = new DijkstraShortestPath<>(graph);
    }

    private void makeGraphFromSections(
            final Graph<String, DefaultWeightedEdge> graph,
            final List<Section> sections
    ) {
        for (final Section section : sections) {
            final Stations stations = section.getStations();

            final String startStation = stations.getCurrent().getName();
            final String endStation = stations.getNext().getName();
            final int distance = stations.getDistance();

            addVertex(graph, startStation, endStation);
            addEdge(graph, startStation, endStation, distance);
        }
    }

    private void addVertex(
            final Graph<String, DefaultWeightedEdge> graph,
            final String startStation,
            final String endStation
    ) {
        graph.addVertex(startStation);
        graph.addVertex(endStation);
    }

    private void addEdge(
            final Graph<String, DefaultWeightedEdge> graph, final String startStation,
            final String endStation, final int distance
    ) {
        graph.setEdgeWeight(
                graph.addEdge(startStation, endStation), distance);
        graph.setEdgeWeight(
                graph.addEdge(endStation, startStation), distance);
    }

    public List<String> findShortestRoute(
            final String startStation,
            final String endStation
    ) {
        return dijkstraGraph.getPath(startStation, endStation).getVertexList();
    }

    public double findShortestRouteDistance(
            final String startStation,
            final String endStation
    ) {
        return dijkstraGraph.getPathWeight(startStation, endStation);
    }
}
