package subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.MultiRoutedStations;
import subway.domain.RoutedStations;
import subway.domain.Station;
import subway.domain.exception.RequestDataNotFoundException;
import subway.dto.RouteResponse;
import subway.dto.StationResponse;

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
        Station sourceStation = stationDao.findById(sourceStationId)
                .orElseThrow(() -> new RequestDataNotFoundException("출발 역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById(targetStationId)
                .orElseThrow(() -> new RequestDataNotFoundException("도착 역이 존재하지 않습니다."));

        Map<Line, RoutedStations> sectionsByLine = lineDao.findAll()
                .stream()
                .collect(Collectors.toMap(line -> line,
                        line -> RoutedStations.from(sectionDao.findByLineId(line.getId()))));

        MultiRoutedStations multiRoutedStations = MultiRoutedStations.from(sectionsByLine);

        // TODO 최단경로 구해서 거리, 요금 계산 후 반환
        // TODO 해당 역의 어느 호선 열차를 타는지도 알려줘야 할까?
        return new RouteResponse(List.of(new StationResponse(1L, "시청역")), 0, 1000);
    }
}
