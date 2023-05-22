package subway.application;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.BasicFareCalculator;
import subway.domain.MultiRoutedStations;
import subway.domain.RouteInfo;
import subway.domain.RoutedStations;
import subway.domain.SubwayMap;
import subway.domain.TransferableRoute;
import subway.domain.entity.Line;
import subway.domain.entity.Station;
import subway.domain.exception.RequestDataNotFoundException;
import subway.dto.RouteResponse;

@Service
public class RouteService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public RouteService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public RouteResponse findShortestRoute(final Long sourceStationId, final Long targetStationId) {
        // TODO LineService와 중복 로직
        Station sourceStation = stationDao.findById(sourceStationId)
                .orElseThrow(() -> new RequestDataNotFoundException("출발 역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById(targetStationId)
                .orElseThrow(() -> new RequestDataNotFoundException("도착 역이 존재하지 않습니다."));

        Map<Line, RoutedStations> sectionsByLine = findSectionsByLine();
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));
        TransferableRoute transferableRoute = subwayMap.findShortestRoute(sourceStation, targetStation);
        RouteInfo routeInfo = RouteInfo.from(transferableRoute, new BasicFareCalculator());
        return RouteResponse.from(routeInfo);
    }

    private Map<Line, RoutedStations> findSectionsByLine() {
        return lineDao.findAll()
                .stream()
                .collect(Collectors.toMap(line -> line,
                        line -> RoutedStations.from(sectionDao.findByLineId(line.getId()))));
    }
}
