package subway.domain.pathfinder;

import java.util.List;
import java.util.Map;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.LineStation;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Sections;
import subway.domain.subwaymap.Station;

@Component
public class JgraphtShortestPathFinder implements SubwayMapShortestPathFinder {

    private DijkstraShortestPath<LineStation, DefaultEdge> shortestPath;
    private final WeightedMultigraph<LineStation, DefaultEdge> graph = new WeightedMultigraph<>(DefaultEdge.class);

    @Override
    public ShortestPath getShortestPath(final Map<Line, Sections> linesAndSections, final Station start,
        final Station end) {
        initialize(linesAndSections);

        final GraphPath<LineStation, DefaultEdge> path = shortestPath.getPath(LineStation.withNullLine(start),
            LineStation.withNullLine(end));

        return new ShortestPath(path.getVertexList(), (int) path.getWeight());
    }

    private void initialize(final Map<Line, Sections> linesAndSections) {
        graph.removeAllEdges(graph.edgeSet());
        graph.removeAllVertices(graph.vertexSet());

        linesAndSections.forEach((line, sections) -> {
            addSectionStationsToVertex(graph, line, sections.getStations());
            addSectionsToEdge(graph, line, sections.getSections());
        });

        shortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addSectionStationsToVertex(final WeightedMultigraph<LineStation, DefaultEdge> graph, final Line line,
        final List<Station> stations) {
        stations.forEach((station) -> graph.addVertex(LineStation.of(line, station)));
    }

    private void addSectionsToEdge(final WeightedMultigraph<LineStation, DefaultEdge> graph, final Line line,
        final List<Section> sections) {
        sections.forEach(section -> {
            final LineStation upStation = LineStation.of(line, section.getUpStation());
            final LineStation downStation = LineStation.of(line, section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        });
    }
}
