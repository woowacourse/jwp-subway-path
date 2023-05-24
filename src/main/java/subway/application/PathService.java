package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.graph.Graph;
import subway.domain.graph.JgraphtGraph;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
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
        Station srcStation = obtainStation(shortestPathRequest.getSrcStationId());
        Station dstStation = obtainStation(shortestPathRequest.getDstStationId());

        Graph graph = new JgraphtGraph(sections);
        List<Station> dijkstraShortestPath = graph.findShortestPath(srcStation, dstStation);
        int shortestPathWeight = graph.findShortestPathWeight(srcStation, dstStation);
        int fare = fareCalculator.calculateFare(shortestPathWeight);

        List<StationResponse> dijkstraShortestPathStations = dijkstraShortestPath.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new ShortestPathResponse(dijkstraShortestPathStations, shortestPathWeight, fare);
    }

    private Station obtainStation(final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 역 ID 입니다."));
        return new Station(stationEntity.getId(), stationEntity .getName());
    }
}
