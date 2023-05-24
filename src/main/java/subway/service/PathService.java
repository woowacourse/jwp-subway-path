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
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PathService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }
    
    public ShortestPathResponse findShortestPath(Long id, ShortestPathRequest request) {
        List<StationEntity> stationsEntity = lineDao.findAllStationsById(id);
        StationEntity.convertToStations(stationsEntity);

        WeightedMultigraph graph = createGraphForPath(stationsEntity);
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);
        List<String> path = shortestPath.getPath(request.getStartName(), request.getDestinationName()).getVertexList();
        double distance = shortestPath.getPathWeight(request.getStartName(), request.getDestinationName());
        int cost = calculateCost(distance);

        return new ShortestPathResponse(path, distance, cost);
    }

    private int calculateCost(double distance) {
        if (distance < 10) {
            return 1250;
        } else if (distance < 50) {
            return 1250 + chargeFirstExtraFee(distance, 5);

        } else {
            return 1250 + chargeFirstExtraFee(distance, 5) + chargeFirstExtraFee(distance, 8);
        }

    }

    private int chargeFirstExtraFee(double distance, int maxDistance) {
        int total = (int) distance - 10;
        int count = 0;
        do {
            count++;
        } while ((total -= maxDistance) > maxDistance);
        return (100 * count);
    }

    private WeightedMultigraph createGraphForPath(List<StationEntity> stationsEntity) {
        WeightedMultigraph graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(stationsEntity, graph);
        addEdgeWeight(stationsEntity, graph);

        return graph;
    }

    private void addVertex(List<StationEntity> stationsEntity, WeightedMultigraph graph) {
        for (StationEntity stationEntity : stationsEntity) {
            graph.addVertex(stationEntity.getName());
        }
    }

    private void addEdgeWeight(List<StationEntity> stationsEntity, WeightedMultigraph graph) {
        for (StationEntity stationEntity : stationsEntity) {
            Optional<StationEntity> nextStation = stationDao.findStationEntityById(stationEntity.getNextStationId());
            Integer distance = findDistance(stationEntity, nextStation);

            if (nextStation.isPresent()) {
                graph.setEdgeWeight(graph.addEdge(stationEntity.getName(), nextStation.get().getName()), distance);
            }
        }
    }

    private Integer findDistance(StationEntity stationEntity, Optional<StationEntity> nextStation) {
        Integer distance;

        if (nextStation.isPresent()) {
            distance = stationEntity.getDistance();
        } else {
            distance = 100;
        }
        return distance;
    }
}
