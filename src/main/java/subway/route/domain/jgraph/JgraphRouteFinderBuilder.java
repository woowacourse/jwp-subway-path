package subway.route.domain.jgraph;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import subway.line.domain.Line;
import subway.line.domain.MiddleSection;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteFinderBuilder;
import subway.route.domain.RouteSegment;
import subway.station.domain.Station;

import java.util.List;

public class JgraphRouteFinderBuilder implements RouteFinderBuilder<RouteSegment> {

    @Override
    public RouteFinder<RouteSegment> buildRouteFinder(List<Line> lines) {
        final Graph<Station, WeightedEdgeWithLineInfo> subwayGraph = new DefaultUndirectedWeightedGraph<>(WeightedEdgeWithLineInfo.class);
        buildGraph(subwayGraph, lines);

        final DijkstraShortestPath<Station, WeightedEdgeWithLineInfo> shortestPath = new DijkstraShortestPath<>(subwayGraph);
        return new JgraphRouteFinder(shortestPath);
    }

    private void buildGraph(Graph<Station, WeightedEdgeWithLineInfo> subwayGraph, List<Line> lines) {
        for (Line line : lines) {
            configureEdgeWithVertices(subwayGraph, line);
        }
    }

    private void configureEdgeWithVertices(Graph<Station, WeightedEdgeWithLineInfo> subwayGraph, Line line) {
        for (MiddleSection section : line.getSections()) {
            subwayGraph.addVertex(section.getUpstream());
            subwayGraph.addVertex(section.getDownstream());

            final WeightedEdgeWithLineInfo weightedEdgeWithLineInfo = new WeightedEdgeWithLineInfo(line.getId(), line.getName());
            subwayGraph.addEdge(section.getUpstream(), section.getDownstream(), weightedEdgeWithLineInfo);
        }
    }
}
