package subway.application;

import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

public class PathService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(PathRequest request) {
        validateStation(request);

        return new PathResponse();
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
