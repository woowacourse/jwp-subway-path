package subway.domain;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class SubwayGraph {

    private final Graph<Station, DefaultWeightedEdge> graph;

    private SubwayGraph(Graph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SubwayGraph from(Subway subway) {
        final Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Station station : subway.getAllStations()) {
            graph.addVertex(station);
        }
        for (final Line line : subway.getLines()) {
            setEdgeWeight(graph, line);
        }
        return new SubwayGraph(graph);
    }

    private static void setEdgeWeight(final Graph<Station, DefaultWeightedEdge> graph, final Line line) {
        for (final Section section : line.getSections()) {
            final DefaultWeightedEdge edge = graph.addEdge(section.getSource(), section.getTarget());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    public List<Station> findShortestPath(Station sourceStation, Station targetStation) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int calculateShortestDistance(Station sourceStation, Station targetStation) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
    }

}
