package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<String, DefaultWeightedEdge> subwayMap;

    public PathFinder(final WeightedMultigraph<String, DefaultWeightedEdge> subwayGraph) {
        this.subwayMap = subwayGraph;
    }

    public static PathFinder generate(final List<Station> stations, final List<Line> lines) {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.forEach(station -> graph.addVertex(station.getName()));

        for (final Line line : lines) {
            line.getSections().
                    forEach(section -> {
                        final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName());
                        graph.setEdgeWeight(edge, section.getDistance());
                    });
        }

        return new PathFinder(graph);
    }

    public Path findPath(final Station departure, final Station arrival) {
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayMap);
        final List<String> shortestPath
                = dijkstraShortestPath.getPath(departure.getName(), arrival.getName()).getVertexList();
        final int pathWeight = (int) dijkstraShortestPath.getPathWeight(departure.getName(), arrival.getName());

        return new Path(shortestPath, pathWeight);
    }
}
