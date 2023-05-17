package subway.application;

import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

public class PathService {

    public PathService(StationDao stationDao, LineDao lineDao) {

    }

    public PathResponse findPath(PathRequest request){
        return new PathResponse();
    }
}
