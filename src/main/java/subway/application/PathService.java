package subway.application;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Charge;
import subway.domain.DiscountRate;
import subway.domain.Distance;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

@Transactional
@Service
public class PathService {

    Distance FIVE = new Distance(5);
    Distance EIGHT = new Distance(8);
    Distance TEN = new Distance(10);
    Distance FOURTH = new Distance(40);
    Distance FIFTH = new Distance(50);

    Charge EXTRA_CHARGE_UNIT = new Charge(100);
    public static final int BASIC_CHARGE = 1250;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(PathRequest request) {
        validateStation(request);
        GraphPath<String, DefaultWeightedEdge> path = find(request);
        validateCanConnect(path);
        List<LineEntity> lines = lineDao.findAll();
        List<String> trace = path.getVertexList();
        int maxExtraCharge = lines.stream().filter(line -> {
            List<StationEntity> stations = stationDao.findByLineId(line.getId());
            return stations.stream().filter((station) -> trace.contains(station.getName())).count()
                > 1;
        }).mapToInt(LineEntity::getExtraCharge).max().getAsInt();
        Distance distance = new Distance((int) path.getWeight());
        Charge charge = calculateCharge(distance, maxExtraCharge);
        Charge teenagerCharge = charge.discount(DiscountRate.TEENAGER_DISCOUNT_RATE);
        Charge childCharge = charge.discount(DiscountRate.CHILD_DISCOUNT_RATE);
        return PathResponse.of(trace, distance, charge, teenagerCharge, childCharge);
    }

    private static void validateCanConnect(GraphPath<String, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("두 역은 연결되어 있지 않습니다");
        }
    }

    private GraphPath<String, DefaultWeightedEdge> find(PathRequest request) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);
        List<StationEntity> stations = stationDao.findAll();
        stations.forEach(station -> {
            if (!graph.containsVertex(station.getName())) {
                graph.addVertex(station.getName());
            }
            if (stations.stream().anyMatch((s) -> s.getId() == station.getNext())) {
                String nextStationName = stations.stream()
                    .filter((s) -> s.getId() == station.getNext()).findFirst().get().getName();
                graph.addVertex(nextStationName);
                graph.setEdgeWeight(graph.addEdge(station.getName(), nextStationName),
                    station.getDistance());
            }
        });

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);

        return dijkstraShortestPath.getPath(request.getStartStation(),
            request.getEndStation());
    }

    private void validateStation(PathRequest request) {
        validateSameStations(request);
        validateExist(request);
    }

    private static void validateSameStations(PathRequest request) {
        if (request.getStartStation().equals(request.getEndStation())) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }

    private void validateExist(PathRequest request) {
        if (stationDao.isNotExist(request.getStartStation()) || stationDao.isNotExist(
            request.getEndStation())) {
            throw new IllegalArgumentException("존재하지 않는 역이 포함되어 있습니다");
        }
    }

    private Charge calculateCharge(Distance distance, int extraCharge) {
        Charge charge = new Charge(BASIC_CHARGE + extraCharge);
        if (distance.isLessThan(TEN)) {
            return charge;
        }
        if (distance.isLessAndEqualsThan(FIFTH)) {
            return charge.add(calculateOverCharge(distance.substract(TEN), FIVE));
        }
        return charge
            .add(calculateOverCharge(FOURTH, FIVE))
            .add(calculateOverCharge(distance.substract(FIFTH), EIGHT));
    }

    private Charge calculateOverCharge(Distance distance, Distance unit) {
        return new Charge(distance.substractOne().divide(unit) + 1).multiply(EXTRA_CHARGE_UNIT);
    }
}
