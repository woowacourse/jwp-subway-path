package subway.domain;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public final class RouteGraph {
    
    private final GraphPath<Long, DefaultWeightedEdge> graphPath;
    
    private RouteGraph(final GraphPath<Long, DefaultWeightedEdge> graphPath) {
        this.graphPath = graphPath;
    }
    
    public static RouteGraph from(final List<Section> sections, final long upTerminalStationId,
            final long downTerminalStationId) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = generateGraph(sections);
        final GraphPath<Long, DefaultWeightedEdge> path = new DijkstraShortestPath<>(subwayMap)
                .getPath(upTerminalStationId, downTerminalStationId);
        return new RouteGraph(path);
    }
    
    private static Graph<Long, DefaultWeightedEdge> generateGraph(final List<Section> sections) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (final Section section : sections) {
            subwayMap.addVertex(section.getUpStationId());
            subwayMap.addVertex(section.getDownStationId());
            final DefaultWeightedEdge edge = subwayMap.addEdge(section.getUpStationId(),
                    section.getDownStationId());
            subwayMap.setEdgeWeight(edge, section.getDistance());
        }
        return subwayMap;
    }
    
    public Route getRoute() {
        return new Route(this.graphPath.getVertexList(), (int) this.graphPath.getWeight());
    }
}