package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fare.FareCalculator;
import subway.domain.path.PathException;
import subway.domain.path.PathFinder;
import subway.domain.Sections;
import subway.domain.Station;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;

@Service
@Transactional
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final FareCalculator fareCalculator;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.fareCalculator = new FareCalculator();
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSourceStationId())
            .orElseThrow(() -> new PathException("등록되지 않은 역과의 경로는 찾을 수 없습니다."));
        Station target = stationDao.findById(pathRequest.getTargetStationId())
            .orElseThrow(() -> new PathException("등록되지 않은 역과의 경로는 찾을 수 없습니다."));

        PathFinder pathFinder = new PathFinder(new Sections(sectionDao.findAll()), source, target);
        List<Station> vertices = pathFinder.getPathVerticies();
        Sections edges = pathFinder.getPathEdges();

        int fare = fareCalculator.calculate(edges);

        return PathResponse.from(fare, vertices);
    }
}
