package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayGraph;
import subway.domain.fare.DistanceFareStrategy;
import subway.domain.fare.FareCalculator;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationDao stationDao;
    private final LineRepository lineRepository;
    private final FareCalculator fareCalculator;

    public PathService(final StationDao stationDao, final LineRepository lineRepository) {
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
        this.fareCalculator = new FareCalculator(new DistanceFareStrategy());
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest shortestPathRequest) {
        List<Section> sections = lineRepository.findSectionsWithSort();
        Map<Long, Station> stationMap = makeStationMap(stationDao.findAll());
        SubwayGraph subwayGraph = new SubwayGraph(sections);

        Station upStation = stationMap.get(shortestPathRequest.getUpStationId());
        Station downStation = stationMap.get(shortestPathRequest.getDownStationId());

        List<String> dijkstraShortestPath = subwayGraph.getDijkstraShortestPath(upStation, downStation);
        int shortestPathWeight = subwayGraph.getShortestPathWeight(upStation, downStation);
        int fare = fareCalculator.calculateFare(shortestPathWeight);
        return new ShortestPathResponse(dijkstraShortestPath, shortestPathWeight, fare);
    }

    private Map<Long, Station> makeStationMap(final List<StationEntity> stationDao) {
        return stationDao.stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        station -> new Station(station.getId(), station.getName())
                ));
    }
}
