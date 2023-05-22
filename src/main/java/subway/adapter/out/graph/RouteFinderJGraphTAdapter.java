package subway.adapter.out.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.application.port.out.route.RouteFinderPort;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Section;
import subway.domain.Station;

@Component
public class RouteFinderJGraphTAdapter implements RouteFinderPort {

    @Override
    public Route findRoute(final Station source, final Station target, final List<Line> lines) {
        if (source.equals(target)) {
            throw new SubwayIllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        List<Section> sections = lines.stream()
                .sorted(Comparator.comparing(line -> line.getSurcharge().getValue()))
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Graph<Station, DefaultWeightedEdge> graph = getSubwayGraph(sections);

        return getRoute(source, target, graph);
    }

    private Graph<Station, DefaultWeightedEdge> getSubwayGraph(final List<Section> sections) {
        Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (final Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
        return graph;
    }

    private Route getRoute(final Station source, final Station target,
            final Graph<Station, DefaultWeightedEdge> graph) {
        try {
            GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph)
                    .getPath(source, target);
            return new Route(path.getVertexList(), (int) path.getWeight());
        } catch (IllegalArgumentException exception) {
            throw new SubwayIllegalArgumentException("경로상에 역이 존재하지 않습니다.");
        } catch (NullPointerException exception) {
            throw new SubwayIllegalArgumentException("도달할 수 없습니다.");
        }
    }
}
