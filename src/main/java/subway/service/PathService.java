package subway.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PathService {

    private static final int MIN_COST = 1250;
    private static final int FIRST_ADDITIONAL_DISTANCE = 5;
    private static final int LAST_ADDITIONAL_DISTANCE = 8;
    private static final int ADDITIONAL_COST = 100;
    private static final int MIN_COST_DISTANCE = 10;
    private static final int MIN_ADDITIONAL_COST_DISTANCE = 50;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public ShortestPathResponse findShortestPath(Long id, ShortestPathRequest request) {
        WeightedMultigraph graph = createGraphForPath();
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);
        List<String> path = shortestPath.getPath(request.getStartName(), request.getDestinationName()).getVertexList();
        double distance = shortestPath.getPathWeight(request.getStartName(), request.getDestinationName());
        int cost = calculateCost(distance);

        return new ShortestPathResponse(path, distance, cost);
    }

    private int calculateCost(double distance) {
        if (distance < MIN_COST_DISTANCE) {
            return MIN_COST;
        } else if (distance < MIN_ADDITIONAL_COST_DISTANCE) {
            return MIN_COST + chargeFirstExtraFee(distance, FIRST_ADDITIONAL_DISTANCE);

        } else {
            return MIN_COST + chargeFirstExtraFee(distance, FIRST_ADDITIONAL_DISTANCE) + chargeFirstExtraFee(distance, LAST_ADDITIONAL_DISTANCE);
        }

    }

    private int chargeFirstExtraFee(double distance, int maxDistance) {
        int total = (int) distance - 10;
        int count = 0;
        do {
            count++;
        } while ((total -= maxDistance) > maxDistance);
        return (ADDITIONAL_COST * count);
    }

    private WeightedMultigraph createGraphForPath() {
        WeightedMultigraph graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(graph);

        for (LineEntity line : lineDao.findAllLines()) {
            List<StationEntity> stationsEntity = lineDao.findAllStationsById(line.getId());
            addEdges(stationsEntity, graph);
        }

        return graph;
    }

    private void addVertex(WeightedMultigraph graph) {
        List<StationEntity> allStations = stationDao.findAll();

        for (StationEntity station : allStations) {
            if (!graph.containsVertex(station.getName())) {
                graph.addVertex(station.getName());
            }
        }
    }

    private void addEdges(List<StationEntity> stationsEntity, WeightedMultigraph graph) {
        for (int i = 0; i < stationsEntity.size() - 1; i++) {
            StationEntity stationEntity = stationsEntity.get(i);
            Optional<StationEntity> next = stationDao.findStationEntityById(stationEntity.getNextStationId());

            String stationName = stationEntity.getName();
            String nextName = next.get().getName();

            graph.setEdgeWeight(graph.addEdge(stationName, nextName), stationEntity.getDistance());
        }
    }
}
