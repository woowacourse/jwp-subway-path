package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineStationAddRequest;
import subway.dto.LineStationInitRequest;
import subway.exception.NotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional
@Service
public class LineStationService {
    
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    
    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }
    
    public void initStations(Long lineId, LineStationInitRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.initLine(
                lineId,
                findStationByName(request.getUpStationName()),
                findStationByName(request.getDownStationName()),
                new Distance(request.getDistance())
        );
        lineRepository.updateLineStation(subway.findLineById(lineId));
    }
    
    public void addStation(Long lineId, LineStationAddRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.addStationToLine(
                lineId,
                findStationByName(request.getBaseStationName()),
                findStationByName(request.getNewStationName()),
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
    
    @Transactional(readOnly = true)
    private Station findStationByName(String name) {
        return stationRepository.findStationByName(name)
                .orElseThrow(()-> new NotFoundException("해당 역이 존재하지 않습니다."));
    }
}
