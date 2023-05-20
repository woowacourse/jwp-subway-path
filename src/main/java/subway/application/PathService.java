package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.path.PathException;
import subway.domain.path.PathFinder;
import subway.domain.path.PathInfo;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final FareCalculator fareCalculator;
    private final PathFinder pathFinder;

    public PathService(StationDao stationDao, SectionDao sectionDao, FareCalculator fareCalculator,
        PathFinder pathFinder) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.fareCalculator = fareCalculator;
        this.pathFinder = pathFinder;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSourceStationId())
            .orElseThrow(() -> new PathException("등록되지 않은 역과의 경로는 찾을 수 없습니다."));
        Station target = stationDao.findById(pathRequest.getTargetStationId())
            .orElseThrow(() -> new PathException("등록되지 않은 역과의 경로는 찾을 수 없습니다."));

        PathInfo pathinfo = pathFinder.findPath(new Sections(sectionDao.findAll()), source, target);
        int fare = fareCalculator.calculate(pathinfo.getPathEdges());
        return PathResponse.from(fare, pathinfo.getPathVerticies());
    }
}
