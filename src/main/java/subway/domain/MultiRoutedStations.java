package subway.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jgrapht.graph.WeightedMultigraph;

public class MultiRoutedStations extends WeightedMultigraph<Station, StationEdge> {

    public MultiRoutedStations(final Class<? extends StationEdge> edgeClass) {
        super(edgeClass);
    }

    public Optional<StationEdge> getEdgeByLine(final Station sourceVertex, final Station targetVertex,
                                               final Line line) {
        Set<StationEdge> allEdges = getAllEdges(sourceVertex, targetVertex);
        if (allEdges.isEmpty()) {
            return Optional.empty();
        }
        return allEdges.stream()
                .filter(edge -> Objects.equals(edge.getLine(), line))
                .findFirst();
    }
}
