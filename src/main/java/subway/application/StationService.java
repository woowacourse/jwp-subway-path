package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationInitialCreateRequest;
import subway.repository.LineStationRepository;

@Service
public class StationService {

    private LineStationRepository lineStationRepository;

    public StationService(LineStationRepository lineStationRepository) {
        this.lineStationRepository = lineStationRepository;
    }

    public List<Station> createInitialStations(Long lineId, StationInitialCreateRequest request) {
        Line line = lineStationRepository.findLineById(lineId);
        line.addInitialStations(
                new Station(null, request.getUpStationName()),
                new Station(null, request.getDownStationName()),
                new Distance(request.getDistance())
        );
        return lineStationRepository.saveInitialStations(line);
    }

    public Station createStation(Long lineId, StationCreateRequest request) {
        Line line = lineStationRepository.findLineById(lineId);
        line.addStation(
                new Station(null, request.getBaseStationName()),
                new Station(null, request.getNewStationName()),
                Direction.findDirection(request.getDirectionOfBaseStation()),
                new Distance(request.getDistance())
        );
        return lineStationRepository.saveStation(line, line.findStationByName(request.getNewStationName()));
    }

    //1. 라인 가져와서 삭제한다음에
    public void removeStation(Long lineId, Long stationId) {
        Line line = lineStationRepository.findLineById(lineId);
        Station stationToRemove = lineStationRepository.findStationById(lineId, stationId);
        line.removeStation(stationToRemove);
        lineStationRepository.removeStation(line, stationToRemove);
    }

}
