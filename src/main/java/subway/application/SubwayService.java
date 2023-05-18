package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.PathResponse;
import subway.persistence.LineRepository;
import subway.persistence.StationDao;

@Service
public class SubwayService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;

    public SubwayService(LineRepository lineRepository, StationDao stationDao) {
        this.lineRepository = lineRepository;
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
        List<Line> allLines = lineRepository.findAll();
        return new Subway(allLines);
    }
}
