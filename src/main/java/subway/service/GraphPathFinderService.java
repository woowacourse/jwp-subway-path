package subway.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.PathSegment;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.dto.service.WeightedLineEdge;
import subway.exception.PathNotExistsException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Component
public class GraphPathFinderService implements PathFinderService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public GraphPathFinderService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Path findPath(Station departure, Station arrival) {
        Graph<Long, WeightedLineEdge> directedGraph = makeGraph();
        GraphPath<Long, WeightedLineEdge> path = getShortestPath(departure, arrival, directedGraph)
                .orElseThrow(PathNotExistsException::new);

        List<WeightedLineEdge> edgeList = path.getEdgeList();
        List<PathSegment> pathSegments = convertToLineSegments(edgeList);
        return new Path(pathSegments);
    }

    private Graph<Long, WeightedLineEdge> makeGraph() {
        Graph<Long, WeightedLineEdge> directedGraph = new DefaultDirectedWeightedGraph<>(WeightedLineEdge.class);

        for (Station station : stationRepository.findAll()) {
            directedGraph.addVertex(station.getId());
        }

        for (Line line : lineRepository.findAll()) {
            addEdgesFromStationEdges(directedGraph, line);
        }
        return directedGraph;
    }

    private void addEdgesFromStationEdges(Graph<Long, WeightedLineEdge> directedGraph, Line line) {
        Deque<StationEdge> stationEdges = new ArrayDeque<>(line.getStationEdges());

        while (true) {
            StationEdge currentStationEdge = stationEdges.pollFirst();
            StationEdge nextStationEdge = stationEdges.peek();
            if (nextStationEdge == null) {
                break;
            }
            WeightedLineEdge edge = new WeightedLineEdge(nextStationEdge, line.getId());
            directedGraph.addEdge(currentStationEdge.getDownStationId(), nextStationEdge.getDownStationId(), edge);
            directedGraph.setEdgeWeight(edge, edge.getWeight());
        }
    }

    private Optional<GraphPath<Long, WeightedLineEdge>> getShortestPath(Station departure, Station arrival,
                                      Graph<Long, WeightedLineEdge> directedGraph) {

        DijkstraShortestPath<Long, WeightedLineEdge> dijkstraShortestPath = new DijkstraShortestPath<>(directedGraph);
        GraphPath<Long, WeightedLineEdge> path = dijkstraShortestPath.getPath(departure.getId(), arrival.getId());

        return Optional.ofNullable(path);
    }

    private PathSegment toLineSegment(Entry<Long, List<WeightedLineEdge>> entry) {
        List<StationEdge> stationEdges = entry.getValue().stream()
                .map(it -> it.getStationEdge())
                .collect(Collectors.toList());

        return new PathSegment(entry.getKey(), stationEdges);
    }


    private List<PathSegment> convertToLineSegments(List<WeightedLineEdge> edgeList) {
        return edgeList.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(WeightedLineEdge::getLineId),
                                lineIdToEdgeList -> lineIdToEdgeList.entrySet().stream()
                                        .map(this::toLineSegment)
                                        .collect(Collectors.toList())
                        )
                );
    }
}
