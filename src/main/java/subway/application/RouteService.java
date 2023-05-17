package subway.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.MultiRoutedStations;
import subway.domain.RoutedStations;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;
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
        SubwayMap subwayMap = new SubwayMap(multiRoutedStations);

        RoutedStations shortestRoutedStations = subwayMap.findShortestRoutedStations(sourceStation, targetStation);
        Distance totalDistance = shortestRoutedStations.totalDistance();
        return new RouteResponse(
                convert(shortestRoutedStations.extractSections()),
                totalDistance.getValue(),
                FareCalculator.calculate(totalDistance));
    }

    private List<StationResponse> convert(List<Section> sections) {
        return sections.stream()
                .map(section -> List.of(section.getLeft(), section.getRight()))
                .flatMap(Collection::stream)
                .distinct()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
