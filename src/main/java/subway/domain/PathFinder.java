package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap;

    public PathFinder(final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph) {
        this.subwayMap = subwayGraph;
    }

    public static PathFinder generate(final List<Line> lines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.forEach(line -> line.getSectionMap().getAllStations()
                .forEach(graph::addVertex));

        for (final Line line : lines) {
            line.getSections().
                    forEach(section -> {
                        final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
                        graph.setEdgeWeight(edge, section.getDistance());
                    });
        }

        return new PathFinder(graph);
    }

    public Path findPath(final Station departure, final Station arrival) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayMap);
        final List<Station> shortestPath
                = dijkstraShortestPath.getPath(departure, arrival).getVertexList();
        final int pathWeight = (int) dijkstraShortestPath.getPathWeight(departure, arrival);

        return new Path(shortestPath, pathWeight);
    }
}
