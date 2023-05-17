package subway.domain.subway.routeMap;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;

public final class DijkstraRouteMap implements RouteMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> paths;

    public DijkstraRouteMap(final List<Line> lines) {
        this.paths = new DijkstraShortestPath<>(setUpGraph(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> setUpGraph(List<Line> lines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        for (Line line : lines) {
            addSectionsIntoGraph(line.getSections(), graph);
        }
        return graph;
    }

    private void addSectionsIntoGraph(final List<Section> sections,
                                      final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            final Station upward = section.getUpward();
            final Station downward = section.getDownward();
            graph.addVertex(upward);
            graph.addVertex(downward);
            graph.setEdgeWeight(graph.addEdge(upward, downward), section.getDistance());
        }
    }

    @Override
    public Path findShortestPath(final Station source, final Station destination) {
        try {
            final GraphPath<Station, DefaultWeightedEdge> path = paths.getPath(source, destination);
            return new Path(path.getVertexList(), (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new InvalidStationException("노선에 등록되지 않은 역 정보입니다.");
        }
    }
}
