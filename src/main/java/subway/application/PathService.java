package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayGraph;
import subway.domain.fare.FareCalculator;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationDao stationDao;
    private final LineRepository lineRepository;
    private final FareCalculator fareCalculator;

    public PathService(final StationDao stationDao, final LineRepository lineRepository, final FareCalculator fareCalculator) {
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
        this.fareCalculator = fareCalculator;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest shortestPathRequest) {
        List<Section> sections = lineRepository.findSectionsWithSort();
        Map<Long, Station> stationMap = makeStationMap(stationDao.findAll());
        SubwayGraph subwayGraph = new SubwayGraph(sections);

        Station upStation = stationMap.get(shortestPathRequest.getSrcStationId());
        Station downStation = stationMap.get(shortestPathRequest.getDstStationId());

        List<Station> dijkstraShortestPath = subwayGraph.getDijkstraShortestPath(upStation, downStation);
        int shortestPathWeight = subwayGraph.getShortestPathWeight(upStation, downStation);
        int fare = fareCalculator.calculateFare(shortestPathWeight);

        List<StationResponse> dijkstraShortestPathStations = dijkstraShortestPath.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new ShortestPathResponse(dijkstraShortestPathStations, shortestPathWeight, fare);
    }

    private Map<Long, Station> makeStationMap(final List<StationEntity> stationDao) {
        return stationDao.stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        station -> new Station(station.getId(), station.getName())
                ));
    }
}
