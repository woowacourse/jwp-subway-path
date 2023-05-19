package subway.domain.subway.route_map;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;

public final class DijkstraRouteMap implements RouteMap {

    private final DijkstraShortestPath<Station, SubwayWeightedEdge> paths;

    public DijkstraRouteMap(final List<Line> lines) {
        this.paths = new DijkstraShortestPath<>(setUpGraph(lines));
    }

    private WeightedMultigraph<Station, SubwayWeightedEdge> setUpGraph(List<Line> lines) {
        final WeightedMultigraph<Station, SubwayWeightedEdge> graph = new WeightedMultigraph<>(
                SubwayWeightedEdge.class);
        for (Line line : lines) {
            addSectionsIntoGraph(line, graph);
        }
        return graph;
    }

    private void addSectionsIntoGraph(final Line line,
                                      final WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
        for (Section section : line.getSections()) {
            final Station upward = section.getUpward();
            final Station downward = section.getDownward();
            graph.addVertex(upward);
            graph.addVertex(downward);
            final SubwayWeightedEdge edge = graph.addEdge(upward, downward);
            edge.setLine(line);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    @Override
    public Path findShortestPath(final Station source, final Station destination) {
        try {
            final GraphPath<Station, SubwayWeightedEdge> path = paths.getPath(source, destination);
            final List<SubwayWeightedEdge> edges = path.getEdgeList();
            final List<Line> borderedLines = edges.stream()
                    .map(SubwayWeightedEdge::getLine)
                    .distinct()
                    .collect(Collectors.toUnmodifiableList());
            return new Path(path.getVertexList(), borderedLines, (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new InvalidStationException("노선에 등록되지 않은 역 정보입니다.");
        }
    }
}
