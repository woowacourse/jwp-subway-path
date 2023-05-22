package subway.domain.subway;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import subway.domain.line.Line;
import subway.domain.section.PathSection;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;

public class SubwayJgraphtGraph implements SubwayGraph {

    private final DijkstraShortestPath dijkstraShortestPath;

    public SubwayJgraphtGraph(final List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath(generateGraph(lines));
    }

    private DefaultDirectedWeightedGraph<Station, LineWeightedEdge> generateGraph(final List<Line> lines) {
        final DefaultDirectedWeightedGraph<Station, LineWeightedEdge> graph =
                new DefaultDirectedWeightedGraph<>(LineWeightedEdge.class);

        for (final Line line : lines) {
            drawGraph(graph, line);
        }
        return graph;
    }

    private void drawGraph(final DefaultDirectedWeightedGraph<Station, LineWeightedEdge> graph, final Line line) {
        for (final Section section : line.getSections()) {
            addVertex(graph, section);
            addEdge(graph, line, section);
        }
    }

    private void addVertex(final DefaultDirectedWeightedGraph<Station, LineWeightedEdge> graph, final Section section) {
        graph.addVertex(section.getUpward());
        graph.addVertex(section.getDownward());
    }

    private void addEdge(
            final DefaultDirectedWeightedGraph<Station, LineWeightedEdge> graph,
            final Line line,
            final Section section
    ) {
        final LineWeightedEdge upwardEdge = graph.addEdge(section.getUpward(), section.getDownward());
        final LineWeightedEdge downwardEdge = graph.addEdge(section.getDownward(), section.getUpward());

        upwardEdge.setLineId(line.getId());
        upwardEdge.setFareOfLine(line.getFare());
        downwardEdge.setLineId(line.getId());
        downwardEdge.setFareOfLine(line.getFare());

        graph.setEdgeWeight(upwardEdge, section.getDistance());
        graph.setEdgeWeight(downwardEdge, section.getDistance());
    }

    @Override
    public List<PathSection> findShortestPathSections(final Station start, final Station end) {
        try {
            final List<LineWeightedEdge> edges = dijkstraShortestPath.getPath(start, end).getEdgeList();
            return edges.stream()
                    .map(PathSection::from)
                    .collect(Collectors.toList());
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
