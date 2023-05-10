package subway.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineStationsResponse;
import subway.dto.StationAddToLineRequest;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void addStationToLine(StationAddToLineRequest stationAddToLineRequest) {
        // TODO 로직 구현

    }

    public void deleteStation(Long lineId, Long stationId) {
        // TODO 로직 구현
    }

    public List<LineStationsResponse> findLineResponses() {
        // TODO 로직 추가
        return null;
    }


    public LineStationsResponse findLineResponseById(Long id) {
        // TODO 로직 추가
        return null;
    }

    public LineResponse saveLine(LineSaveRequest lineSaveRequest) {
        Line line = Line.of(
                lineSaveRequest.getName(),
                lineSaveRequest.getUpwardTerminus(),
                lineSaveRequest.getDownwardTerminus(),
                lineSaveRequest.getDistance()
        );
        return new LineResponse(lineRepository.save(line), line.getName());
    }
}
