package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.subway.Path;
import subway.domain.line.Station;
import subway.domain.subway.Subway;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class SubwayService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SubwayService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public Path findShortestRoute(int passengerAge, Long startStationId, Long endStationId) {
        Station startStation = stationRepository.getStation(startStationId);
        Station endStation = stationRepository.getStation(endStationId);

        List<Line> lines = lineRepository.getAllLines();
        Subway subway = Subway.create(lines);

        return subway.findShortestRoute(passengerAge, startStation, endStation);
    }
}
