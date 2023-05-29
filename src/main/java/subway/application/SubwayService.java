package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.PathResponse;
import subway.persistence.StationDao;

@Service
public class SubwayService {

    private final LineService lineService;
    private final StationDao stationDao;

    public SubwayService(LineService lineService, StationDao stationDao) {
        this.lineService = lineService;
        this.stationDao = stationDao;
    }

    public PathResponse getShortestPath(Long fromStationId, Long toStationId) {
        Subway subway = getSubway();
        Station fromStation = stationDao.findById(fromStationId);
        Station toStation = stationDao.findById(toStationId);

        Path shortestPath = subway.getShortestPath(fromStation, toStation);
        return PathResponse.of(shortestPath);
    }

    private Subway getSubway() {
        List<Line> allLines = lineService.findAll();
        return new Subway(allLines);
    }
}
