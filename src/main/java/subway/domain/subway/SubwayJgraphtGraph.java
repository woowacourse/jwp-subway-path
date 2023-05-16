package subway.domain.subway;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;

public class SubwayJgraphtGraph implements SubwayGraph {

    private final DijkstraShortestPath dijkstraShortestPath;

    public SubwayJgraphtGraph(final List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath(generateGraph(lines));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateGraph(final List<Line> lines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph(DefaultWeightedEdge.class);

        for (final Line line : lines) {
            drawGraph(graph, line);
        }
        return graph;
    }

    private void drawGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Line line) {
        for (final Section section : line.getSections()) {
            final Station upward = section.getUpward();
            final Station downward = section.getDownward();
            graph.addVertex(upward);
            graph.addVertex(downward);
            graph.setEdgeWeight(graph.addEdge(upward, downward), section.getDistance());
        }
    }

    @Override
    public List<Station> findShortestPath(final Station start, final Station end) {
        try {
            return dijkstraShortestPath.getPath(start, end).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new InvalidStationException("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }

    @Override
    public long calculateShortestDistance(final Station start, final Station end) {
        try {
            return (long) dijkstraShortestPath.getPath(start, end).getWeight();
        } catch (IllegalArgumentException e) {
            throw new InvalidStationException("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }
}
