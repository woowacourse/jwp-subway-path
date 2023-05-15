package subway.domain;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.vo.Distance;

public class Path {

    private final Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> algorithm;

    public Path(final Lines lines) {
        addLines(lines);
        algorithm = new DijkstraShortestPath<>(graph);
    }

    private void addLines(final Lines lines) {
        final List<Section> sections = lines.findAllSections();
        for (final Section section : sections) {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            final Distance distance = section.getDistance();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.getValue());
        }
    }

    public List<Station> findShortest(final Station start, final Station end) {
        final GraphPath<Station, DefaultWeightedEdge> path = algorithm.getPath(start, end);
        return path.getVertexList();
    }

    public long findTotalDistanceAsLong(final Station start, final Station end) {
        final GraphPath<Station, DefaultWeightedEdge> path = algorithm.getPath(start, end);
        return (long) path.getWeight();
    }
}
