package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.FareCalculator;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

@Service
public class PathService {
    private final SubwayGraphs subwayGraphs;
    private final StationDao stationDao;

    public PathService(SubwayGraphs subwayGraphs, StationDao stationDao) {
        this.subwayGraphs = subwayGraphs;
        this.stationDao = stationDao;
    }

    public ShortestPathResponse findShortestPath(ShortestPathRequest request) {
        Station startStation = stationDao.findByName(request.getStartStationName())
                .orElseThrow(() -> new StationNotFoundException())
                .toDomain();
        Station endStation = stationDao.findByName(request.getEndStationName())
                .orElseThrow(() -> new StationNotFoundException())
                .toDomain();

        ShortestPath shortestPath = subwayGraphs.getShortestPath(startStation, endStation);
        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(shortestPath.getDistance());
        return new ShortestPathResponse(shortestPath.getPath(), shortestPath.getDistance(), fare);
    }
}
