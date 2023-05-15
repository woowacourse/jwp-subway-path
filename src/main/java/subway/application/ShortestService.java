package subway.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.PathEdge;
import subway.domain.Station;
import subway.dto.ShortestResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ShortestService {

    private final PathDao pathDao;
    private final LineDao lineDao;

    public ShortestService(final PathDao pathDao, final LineDao lineDao) {
        this.pathDao = pathDao;
        this.lineDao = lineDao;
    }

    public ShortestResponse findShortest(final Long startId, final Long endId) {
        final List<Path> paths = findAllPaths();
        final List<Station> stations = findAllStations(paths);

        final DijkstraShortestPath<Station, PathEdge> graph = makeGraph(paths, stations);

        final Station start = findStationById(stations, startId);
        final Station end = findStationById(stations, endId);

        return findOrThrow(graph, start, end);
    }

    private List<Path> findAllPaths() {
        final List<Line> lines = lineDao.findAll();

        return lines.stream()
                .map(pathDao::findByLine)
                .flatMap(paths -> paths.toList().stream())
                .collect(Collectors.toList());
    }

    private List<Station> findAllStations(final List<Path> allPaths) {
        return allPaths.stream()
                .flatMap(path -> Stream.of(path.getUp(), path.getDown()))
                .distinct()
                .collect(Collectors.toList());
    }

    private DijkstraShortestPath<Station, PathEdge> makeGraph(final List<Path> paths, final List<Station> stations) {
        final WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        stations.forEach(graph::addVertex);
        paths.forEach(path -> graph.addEdge(path.getUp(), path.getDown(), PathEdge.from(path)));

        return new DijkstraShortestPath<>(graph);
    }

    private Station findStationById(final List<Station> stations, final Long stationId) {
        return stations.stream()
                .filter(station -> stationId.equals(station.getId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    private ShortestResponse findOrThrow(final DijkstraShortestPath<Station, PathEdge> graph, final Station start, final Station end) {
        try {
            final List<PathEdge> result = graph.getPath(start, end).getEdgeList();

            return ShortestResponse.from(result);
        } catch (final NullPointerException e) {
            throw new IllegalStateException("경로가 존재하지 않습니다.");
        }
    }
}
