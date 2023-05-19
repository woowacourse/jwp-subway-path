package subway.adapter.route.out.find;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.application.route.port.out.find.PathCalculator;
import subway.application.route.port.out.find.PathRequestDto;
import subway.application.route.port.out.find.PathResponseDto;
import subway.domain.route.InterStationEdge;

@Component
public class JgraphtPathCalculator implements PathCalculator {

    @Override
    public PathResponseDto calculatePath(final PathRequestDto requestDto) {
        final DijkstraShortestPath<Long, InterStationEdge> dijkstraShortestPath = drawDijkstraGraph(
                requestDto);
        try {
            final double distance = dijkstraShortestPath.getPathWeight(requestDto.getSourceId(),
                    requestDto.getTargetId());

            final List<InterStationEdge> stations = dijkstraShortestPath.getPath(requestDto.getSourceId(),
                    requestDto.getTargetId()).getEdgeList();
            return new PathResponseDto((int) distance, stations);
        } catch (final Exception e) {
            throw new PathNotFoundException();
        }
    }

    private DijkstraShortestPath<Long, InterStationEdge> drawDijkstraGraph(
            final PathRequestDto requestDto) {
        final WeightedMultigraph<Long, InterStationEdge> graph
                = new WeightedMultigraph<>(InterStationEdge.class);
        drawGraph(requestDto, graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void drawGraph(final PathRequestDto requestDto, final WeightedMultigraph<Long, InterStationEdge> graph) {
        requestDto.getGraph().getEdges().forEach(edge -> {
            graph.addVertex(edge.getUpStationId());
            graph.addVertex(edge.getDownStationId());
            graph.addEdge(edge.getUpStationId(), edge.getDownStationId(), edge);
            graph.setEdgeWeight(edge, edge.getWeight());
        });
    }
}
