package subway.adapter.out.graph;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.application.port.out.route.RouteFinderPort;
import subway.exception.SubwayIllegalArgumentException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.route.Route;
import subway.domain.route.RouteSection;

@Component
public class RouteFinderJGraphTAdapter implements RouteFinderPort {

    @Override
    public Route findRoute(final Station source, final Station target, final List<Line> lines) {
        if (source.equals(target)) {
            throw new SubwayIllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        List<Line> fareOrderedLines = getFareOrderedLines(lines);

        Graph<Station, LineWeightedEdge> graph = getSubwayGraph(fareOrderedLines);

        return getRoute(source, target, graph);
    }

    private List<Line> getFareOrderedLines(final List<Line> lines) {
        return lines.stream()
                .sorted(Comparator.comparing(line -> line.getSurcharge().getValue()))
                .collect(Collectors.toUnmodifiableList());
    }


    private Graph<Station, LineWeightedEdge> getSubwayGraph(final List<Line> lines) {
        Graph<Station, LineWeightedEdge> graph = new WeightedMultigraph<>(LineWeightedEdge.class);

        for (final Line line : lines) {
            for (final Section section : line.getSections()) {
                addSectionToGraph(graph, line, section);
            }
        }
        return graph;
    }

    private void addSectionToGraph(final Graph<Station, LineWeightedEdge> graph, final Line line,
            final Section section) {
        addVertex(graph, section);
        addEdge(graph, line, section);
    }

    private void addVertex(final Graph<Station, LineWeightedEdge> graph, final Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private void addEdge(final Graph<Station, LineWeightedEdge> graph, final Line line, final Section section) {
        LineWeightedEdge edge = new LineWeightedEdge(line, section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        graph.setEdgeWeight(edge, section.getDistance());
    }

    private Route getRoute(final Station source, final Station target,
            final Graph<Station, LineWeightedEdge> graph) {
        try {
            GraphPath<Station, LineWeightedEdge> path = new DijkstraShortestPath<>(graph)
                    .getPath(source, target);
            List<RouteSection> routeSections = path.getEdgeList()
                    .stream()
                    .map(edge -> new RouteSection(edge.getLine(), edge.getSection()))
                    .collect(Collectors.toList());
            return new Route(routeSections);
        } catch (IllegalArgumentException exception) {
            throw new SubwayIllegalArgumentException("경로상에 역이 존재하지 않습니다.");
        } catch (NullPointerException exception) {
            throw new SubwayIllegalArgumentException("도달할 수 없습니다.");
        }
    }
}
