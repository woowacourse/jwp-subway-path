package subway.application;

import java.util.ArrayList;
import java.util.List;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Charge;
import subway.domain.Distance;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

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
        List<String> path = new ArrayList<>(List.of("성수역", "뚝섬역", "잠실역", "건대입구역"));
        Distance distance = new Distance(26);
        Charge charge = calculateCharge(distance);
        return PathResponse.of(path, distance, charge);
    }

    public Charge calculateCharge(Distance distance) {
        Charge charge = new Charge(BASIC_CHARGE);
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
}
