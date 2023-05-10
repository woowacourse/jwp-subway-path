package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineStationsResponse;
import subway.dto.StationAddToLineRequest;

@Service
public class LineService {

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
        // TODO 로직 추가
        return null;
    }
}
