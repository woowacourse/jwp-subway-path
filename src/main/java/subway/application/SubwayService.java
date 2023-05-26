package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SubwayService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(final PathRequest request) {
        List<Station> allStation = stationDao.findAll();
        Map<Long, Station> stationIdMap = allStation.stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station
                ));

        List<Section> allSection = getAllSectoinList(stationIdMap);
        SubwayMap subwayMap = new SubwayMap(allStation, allSection);

        return configurePathResponse(request, stationIdMap, subwayMap);
    }

    private List<Section> getAllSectoinList(final Map<Long, Station> stationIdMap) {
        return sectionDao.findAll()
                .stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getLineId(),
                        stationIdMap.get(sectionEntity.getUpperStation()),
                        stationIdMap.get(sectionEntity.getLowerStation()),
                        new Distance(sectionEntity.getDistance())))
                .collect(Collectors.toList());
    }

    private PathResponse configurePathResponse(final PathRequest request, final Map<Long, Station> stationIdMap, final SubwayMap subwayMap) {
        Station from = stationIdMap.get(request.getFromStation());
        Station to = stationIdMap.get(request.getToStation());
        Sections sections = subwayMap.getShortestPath(from, to);

        List<SectionResponse> sectionResponses = sections.getOrderedSections()
                .stream()
                .map(SectionResponse::from)
                .collect(Collectors.toList());
        int distance = sections.getDistanceBetween(from, to).getValue();
        int fare = FareCalculator.calculate(distance);

        return new PathResponse(sectionResponses, distance, fare);
    }
}
