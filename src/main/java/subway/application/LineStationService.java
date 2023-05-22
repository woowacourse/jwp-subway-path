package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;
import subway.repository.LineRepository;

@Service
public class LineStationService {

    private LineRepository lineRepository;

    public LineStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void initStations(Long lineId, LineStationInitRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.initLine(
                lineId,
                new Station(null, request.getUpStationName()),
                new Station(null, request.getDownStationName()),
                new Distance(request.getDistance())
        );
        lineRepository.updateLineStation(subway.findLineById(lineId));
    }

    public void addStation(Long lineId, LineStationAddRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.addStationToLine(
                lineId,
                new Station(null, request.getBaseStationName()),
                new Station(null, request.getNewStationName()),
                Direction.findDirection(request.getDirectionOfBaseStation()),
                new Distance(request.getDistance())
        );
        lineRepository.updateLineStation(subway.findLineById(lineId));
    }

    public void removeStation(Long lineId, Long stationId) {
        Line line = lineRepository.findLineById(lineId);
        line.removeStation(line.findStationById(stationId));
        lineRepository.updateLineStation(line);
    }

}
