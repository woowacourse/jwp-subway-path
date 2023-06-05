package subway.route.out.find;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.route.application.port.out.find.PathCalculator;
import subway.route.application.port.out.find.PathRequestDto;
import subway.route.application.port.out.find.PathResponseDto;
import subway.route.domain.InterStationEdge;

@Component
public class JgraphtPathCalculator implements PathCalculator {

    @Override
    public PathResponseDto calculatePath(PathRequestDto requestDto) {
        DijkstraShortestPath<Long, InterStationEdge> dijkstraShortestPath = drawDijkstraGraph(
                requestDto);
        try {
            double distance = dijkstraShortestPath.getPathWeight(requestDto.getSourceId(),
                    requestDto.getTargetId());

            List<InterStationEdge> stations = dijkstraShortestPath.getPath(requestDto.getSourceId(),
                    requestDto.getTargetId()).getEdgeList();
            return new PathResponseDto((int) distance, stations);
        } catch (Exception e) {
            throw new PathNotFoundException();
        }
    }

    private DijkstraShortestPath<Long, InterStationEdge> drawDijkstraGraph(
            PathRequestDto requestDto) {
        WeightedMultigraph<Long, InterStationEdge> graph
                = new WeightedMultigraph<>(InterStationEdge.class);
        drawGraph(requestDto, graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void drawGraph(PathRequestDto requestDto, WeightedMultigraph<Long, InterStationEdge> graph) {
        requestDto.getGraph().getEdges().forEach(edge -> {
            graph.addVertex(edge.getUpStationId());
            graph.addVertex(edge.getDownStationId());
            graph.addEdge(edge.getUpStationId(), edge.getDownStationId(), edge);
            graph.setEdgeWeight(edge, edge.getWeight());
        });
    }
}
