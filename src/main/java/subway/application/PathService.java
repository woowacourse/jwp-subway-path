package subway.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private static final Distance DISTANCE_FIVE_UNIT = new Distance(5);
    private static final Distance DISTANCE_EIGHT_UNIT = new Distance(8);
    private static final Distance FIRST_CHARGE_BOUND = new Distance(10);
    private static final Distance SECOND_CHARGE_BOUND = new Distance(50);
    private static final Charge EXTRA_CHARGE_UNIT = new Charge(100);
    public static final int BASIC_CHARGE = 1250;
    public static final int MIN_COUNT_TO_TRANSFER = 2;

    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(PathRequest request) {
        validateSourceAndDestination(request);
        GraphPath<String, DefaultWeightedEdge> path = generateShortestPath(request);
        validateReachable(path);
        List<LineEntity> lines = lineDao.findAll();
        List<String> trace = path.getVertexList();
        Distance distance = new Distance((int) path.getWeight());
        Charge charge = calculateCharge(distance, findMaxExtraChargeInAllLines(lines, trace));
        Charge teenagerCharge = charge.discount(DiscountRate.TEENAGER_DISCOUNT_RATE);
        Charge childCharge = charge.discount(DiscountRate.CHILD_DISCOUNT_RATE);
        return PathResponse.of(trace, distance, charge, teenagerCharge, childCharge);
    }

    private void validateSourceAndDestination(PathRequest request) {
        validateSameStations(request);
        validateExist(request);
    }

    private void validateSameStations(PathRequest request) {
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

    private GraphPath<String, DefaultWeightedEdge> generateShortestPath(PathRequest request) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = generateSubwayGraph();
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        return dijkstraShortestPath.getPath(request.getStartStation(),
            request.getEndStation());
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> generateSubwayGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<StationEntity> stations = stationDao.findAll();
        stations.forEach(station -> {
            Optional<StationEntity> nextStation = stations.stream()
                .filter((next) -> Objects.equals(next.getId(), station.getNext())).findFirst();
            nextStation.ifPresent(entity -> addVertexAndEdge(graph, station, entity.getName()));
        });
        return graph;
    }

    private void addVertexAndEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph,
        StationEntity station, String nextStationName) {
        graph.addVertex(station.getName());
        graph.addVertex(nextStationName);
        graph.setEdgeWeight(graph.addEdge(station.getName(), nextStationName),
            station.getDistance());
    }

    private void validateReachable(GraphPath<String, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("두 역은 연결되어 있지 않습니다");
        }
    }

    private Charge calculateCharge(Distance distance, Charge extraCharge) {
        Charge charge = new Charge(BASIC_CHARGE).add(extraCharge);
        if (distance.isLessThan(FIRST_CHARGE_BOUND)) {
            return charge;
        }
        if (distance.isLessAndEqualsThan(SECOND_CHARGE_BOUND)) {
            return charge.add(calculateOverCharge(distance.substract(FIRST_CHARGE_BOUND),
                DISTANCE_FIVE_UNIT));
        }
        return charge.add(calculateOverCharge(SECOND_CHARGE_BOUND.substract(FIRST_CHARGE_BOUND),
                DISTANCE_FIVE_UNIT))
            .add(calculateOverCharge(distance.substract(SECOND_CHARGE_BOUND), DISTANCE_EIGHT_UNIT));
    }

    private Charge calculateOverCharge(Distance distance, Distance unit) {
        return new Charge(distance.substractOne().divide(unit) + 1).multiply(EXTRA_CHARGE_UNIT);
    }

    private Charge findMaxExtraChargeInAllLines(List<LineEntity> lines, List<String> trace) {
        int maxExtraCharge = lines.stream()
            .filter(line -> isTransferLine(trace, stationDao.findByLineId(line.getId())))
            .mapToInt(LineEntity::getExtraCharge).max().getAsInt();
        return new Charge(maxExtraCharge);
    }

    private boolean isTransferLine(List<String> trace, List<StationEntity> stations) {
        return stations.stream().filter(
            (station) -> trace.contains(station.getName())).count() >= MIN_COUNT_TO_TRANSFER;
    }
}
