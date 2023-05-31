package subway.route.domain.jgraph;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.line.domain.MiddleSection;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteFinderBuilder;
import subway.route.domain.RouteSegment;
import subway.station.domain.Station;

import java.util.List;

@Component
public class JgraphRouteFinderBuilder implements RouteFinderBuilder<RouteSegment> {

    @Override
    public RouteFinder<RouteSegment> buildRouteFinder(List<Line> lines) {
        final Graph<Station, WeightedEdgeWithLineMetadata> subwayGraph = new DefaultUndirectedWeightedGraph<>(WeightedEdgeWithLineMetadata.class);
        buildGraph(subwayGraph, lines);

        final DijkstraShortestPath<Station, WeightedEdgeWithLineMetadata> shortestPath = new DijkstraShortestPath<>(subwayGraph);
        return new JgraphRouteFinder(shortestPath);
    }

    private void buildGraph(Graph<Station, WeightedEdgeWithLineMetadata> subwayGraph, List<Line> lines) {
        for (Line line : lines) {
            configureEdgeWithVertices(subwayGraph, line);
        }
    }

    private void configureEdgeWithVertices(Graph<Station, WeightedEdgeWithLineMetadata> subwayGraph, Line line) {
        for (MiddleSection section : line.getSections()) {
            subwayGraph.addVertex(section.getUpstream());
            subwayGraph.addVertex(section.getDownstream());

            final WeightedEdgeWithLineMetadata weightedEdgeWithLineMetadata = new WeightedEdgeWithLineMetadata(line.getId(), line.getLineInfo(), section.getDistance());
            subwayGraph.setEdgeWeight(weightedEdgeWithLineMetadata, section.getDistance());
            subwayGraph.addEdge(section.getUpstream(), section.getDownstream(), weightedEdgeWithLineMetadata);
        }
    }
}
