package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dao.SubwayDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.RouteMap;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationEnrollRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final SubwayDao subwayDao;
    private final StationDao stationDao;

    public SubwayService(SubwayDao subwayDao, StationDao stationDao) {
        this.subwayDao = subwayDao;
        this.stationDao = stationDao;
    }

    public void enrollStation(Integer lineId, StationEnrollRequest request) {
        Line line = subwayDao.findById(lineId);
        Section section = new Section(
                stationDao.findById(request.getFromStation()),
                stationDao.findById(request.getToStation()),
                new Distance(request.getDistance())
        );
        line.addSection(section);
        subwayDao.save(line);
    }

    public void deleteStation(Integer lineId, Long stationId) {
        Line line = subwayDao.findById(lineId);

        line.deleteStation(stationDao.findById(stationId));
        subwayDao.save(line);
    }

    public List<StationResponse> findRouteMap(Integer lineId) {
        Line line = subwayDao.findById(lineId);
        return line.routeMap()
                .getRouteMap()
                .stream()
                .map(station -> new StationResponse(
                        station.getId(),
                        station.getName()
                )).collect(Collectors.toList());
    }
}
