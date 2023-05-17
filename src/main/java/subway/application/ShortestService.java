package subway.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.PathDao;
import subway.dao.StationDao;
import subway.domain.FareStrategy;
import subway.domain.Station;
import subway.domain.path.Path;
import subway.domain.path.PathEdgeProxy;
import subway.domain.path.Paths;
import subway.dto.ShortestResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShortestService {

    private final PathDao pathDao;
    private final StationDao stationDao;
    private final FareStrategy fareStrategy;

    public ShortestService(final PathDao pathDao, StationDao stationDao, final FareStrategy fareStrategy) {
        this.pathDao = pathDao;
        this.stationDao = stationDao;
        this.fareStrategy = fareStrategy;
    }

    public ShortestResponse findShortest(final Long startId, final Long endId) {
        final Paths paths = pathDao.findAll();
        final DijkstraShortestPath<Station, PathEdgeProxy> graph = makeGraph(paths);

        final Station start = stationDao.findById(startId);
        final Station end = stationDao.findById(endId);

        final Paths shortest = findOrThrow(graph, start, end);
        final int fare = fareStrategy.calculate(shortest.getTotalDistance());
        return ShortestResponse.of(shortest, fare);
    }

    private DijkstraShortestPath<Station, PathEdgeProxy> makeGraph(final Paths paths) {
        final WeightedMultigraph<Station, PathEdgeProxy> graph = new WeightedMultigraph<>(PathEdgeProxy.class);
        paths.getStations().forEach(graph::addVertex);
        paths.toList()
                .forEach(path -> graph.addEdge(path.getUp(), path.getDown(), PathEdgeProxy.from(path)));

        return new DijkstraShortestPath<>(graph);
    }

    private Paths findOrThrow(final DijkstraShortestPath<Station, PathEdgeProxy> graph, final Station start, final Station end) {
        try {
            final List<PathEdgeProxy> result = graph.getPath(start, end).getEdgeList();

            List<Path> paths = result.stream()
                    .map(PathEdgeProxy::toPath)
                    .collect(Collectors.toList());
            return new Paths(paths);
        } catch (final Exception e) {
            throw new IllegalStateException("경로가 존재하지 않습니다.");
        }
    }
}
