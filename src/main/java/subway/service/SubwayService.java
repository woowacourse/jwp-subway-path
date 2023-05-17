package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.Subway;
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
    public Path findShortestRoute(Long startStationId, Long endStationId) {
        // TODO : 존재하는 역이지만 노선에 등록되지 않았을 경우는 어떻게 해야할까?
        Station startStation = stationRepository.getStation(startStationId);
        Station endStation = stationRepository.getStation(endStationId);

        List<Station> stations = stationRepository.getAllStations();
        List<Line> lines = lineRepository.getAllLines();
        Subway subway = Subway.create(stations, lines);

        // TODO : 노선에 등록되어 있지만, 경로를 찾을 수 없을 때는 어떻게 해야할까? -> NPE 터지네
        return subway.findShortestRoute(startStation, endStation);
    }
}
