package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

public class SubwayGraph {

    private SubwayGraph() {
    }

    public static DijkstraShortestPath<Station, Section> getShortestPath(final List<Line> lines) {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        for (final Line line : lines) {
            line.stations().forEach(v -> {
                if (!graph.containsVertex(v)) {
                    graph.addVertex(v);
                }
            });
            final List<Section> sections = line.sections();
            line.sections()
                    .forEach(section -> {
                        final Section addedSection = graph.addEdge(section.getUpStation(), section.getDownStation());
                        graph.setEdgeWeight(addedSection, section.getDistanceValue());
                    });
        }

        return new DijkstraShortestPath<>(graph);
    }
}
