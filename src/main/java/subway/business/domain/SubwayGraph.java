package subway.business.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {
    private final WeightedGraph graph;

    public SubwayGraph(WeightedGraph graph) {
        this.graph = graph;
    }

    public static SubwayGraph from(Subway subway) {
        WeightedGraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        subway.getStations().forEach(graph::addVertex);
        subway.getSections().forEach(section -> setEdgeWeightFromSection(graph, section));
        return new SubwayGraph(graph);
    }

    private static void setEdgeWeightFromSection(WeightedGraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getUpwardStation(), section.getDownwardStation()),
                section.getDistance());
    }

    public int getTotalDistance(Station sourceStation, Station destStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        return (int) path.getPathWeight(sourceStation, destStation);
    }

    public List<Station> getShortestPath(Station sourceStation, Station destStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        return path.getPath(sourceStation, destStation).getVertexList();
    }
}
