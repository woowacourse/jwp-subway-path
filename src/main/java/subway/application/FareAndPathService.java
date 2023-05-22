package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicyRelatedParameters;
import subway.domain.path.PathException;
import subway.domain.path.PathFinder;
import subway.domain.path.PathInfo;
import subway.ui.dto.FareAndPathResponse;
import subway.ui.dto.FareAndPathRequest;

@Service
public class FareAndPathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final FareCalculator fareCalculator;
    private final PathFinder pathFinder;

    public FareAndPathService(StationDao stationDao, SectionDao sectionDao, FareCalculator fareCalculator,
        PathFinder pathFinder) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.fareCalculator = fareCalculator;
        this.pathFinder = pathFinder;
    }

    @Transactional(readOnly = true)
    public FareAndPathResponse findFareAndPath(FareAndPathRequest request) {
        Station source = getStation(request.getSourceStationId());
        Station target = getStation(request.getTargetStationId());

        Sections sections = new Sections(sectionDao.findAll());
        PathInfo pathinfo = pathFinder.findPath(sections, source, target);
        FarePolicyRelatedParameters parameters = createFareRelatedParameters(request, pathinfo);
        int fare = fareCalculator.calculate(parameters);
        return FareAndPathResponse.from(fare, pathinfo.getPathVerticies());
    }

    private Station getStation(Long request) {
        return stationDao.findById(request)
            .orElseThrow(() -> new PathException("등록되지 않은 역과의 경로는 찾을 수 없습니다."));
    }

    private FarePolicyRelatedParameters createFareRelatedParameters(FareAndPathRequest request, PathInfo pathinfo) {
        return new FarePolicyRelatedParameters.Builder(pathinfo.getPathEdges())
            .age(request.getAge())
            .build();
    }
}
