package subway.route.jgrapht;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.route.domain.InterStationEdge;
import subway.route.domain.PathCalculator;
import subway.route.domain.exception.PathNotFoundException;
import subway.route.dto.request.PathRequest;
import subway.route.dto.response.PathResponse;

@Component
public class JgraphtPathCalculator implements PathCalculator {

    @Override
    public PathResponse calculatePath(PathRequest requestDto) {
        DijkstraShortestPath<Long, InterStationEdge> dijkstraShortestPath = drawDijkstraGraph(
                requestDto);
        try {
            double distance = dijkstraShortestPath.getPathWeight(requestDto.getSourceId(),
                    requestDto.getTargetId());

            List<InterStationEdge> stations = dijkstraShortestPath.getPath(requestDto.getSourceId(),
                    requestDto.getTargetId()).getEdgeList();
            return new PathResponse((int) distance, stations);
        } catch (Exception e) {
            throw new PathNotFoundException();
        }
    }

    private DijkstraShortestPath<Long, InterStationEdge> drawDijkstraGraph(
            PathRequest requestDto) {
        WeightedMultigraph<Long, InterStationEdge> graph
                = new WeightedMultigraph<>(InterStationEdge.class);
        drawGraph(requestDto, graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void drawGraph(PathRequest requestDto, WeightedMultigraph<Long, InterStationEdge> graph) {
        requestDto.getGraph().forEach(edge -> {
            graph.addVertex(edge.getUpStationId());
            graph.addVertex(edge.getDownStationId());
            graph.addEdge(edge.getUpStationId(), edge.getDownStationId(), edge);
            graph.setEdgeWeight(edge, edge.getWeight());
        });
    }
}
