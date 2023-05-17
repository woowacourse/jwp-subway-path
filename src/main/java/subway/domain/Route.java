package subway.domain;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public final class Route {
    
    private final Graph<Long, DefaultWeightedEdge> subwayMap;
    
    private Route(final Graph<Long, DefaultWeightedEdge> subwayMap) {
        this.subwayMap = subwayMap;
    }
    
    public static Route from(final List<Section> sections) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = generateGraph(sections);
        return new Route(subwayMap);
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
    
    public List<Long> findRoute(final long upTerminalStationId,
            final long downTerminalStationId) {
        return new DijkstraShortestPath<>(this.subwayMap)
                .getPath(upTerminalStationId, downTerminalStationId)
                .getVertexList();
    }
}