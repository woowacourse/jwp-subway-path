package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.Direction;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void addStationToLine(long lineId, StationAddToLineRequest stationAddToLineRequest) {
        Line line = lineRepository.findById(lineId);
        line.addStation(
                stationAddToLineRequest.getStation(),
                stationAddToLineRequest.getNeighborhoodStation(),
                Direction.from(stationAddToLineRequest.getAddDirection()),
                stationAddToLineRequest.getDistance()
        );
        lineRepository.update(line);
    }

    public void deleteStation(Long lineId, String stationName) {
        Line line = lineRepository.findById(lineId);
        line.deleteStation(stationName);
        lineRepository.update(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        return LineResponse.from(lineRepository.findById(id));
    }

    public LineResponse createLine(LineSaveRequest lineSaveRequest) {
        Line line = Line.createToSave(
                lineSaveRequest.getName(),
                lineSaveRequest.getUpwardTerminus(),
                lineSaveRequest.getDownwardTerminus(),
                lineSaveRequest.getDistance()
        );
        long savedLineId = lineRepository.create(line);
        return LineResponse.from(lineRepository.findById(savedLineId));
    }
}
