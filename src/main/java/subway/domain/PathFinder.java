package subway.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final List<Section> sections;

    public PathFinder(final List<Section> sections) {
        this.sections = sections;
    }

    public Path findShortestPath(final Station from, final Station to) {
        final GraphPath<Station, DefaultWeightedEdge> shortestPath = getDijkstraShortestPath().getPath(from, to);
        return new Path(shortestPath.getVertexList(), shortestPath.getWeight());
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance().getValue());
        });
        return new DijkstraShortestPath<>(graph);
    }
}
