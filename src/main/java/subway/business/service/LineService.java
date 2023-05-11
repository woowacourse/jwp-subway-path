package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.domain.Section;
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
        Line line = lineRepository.findById(id);
        List<Section> sections = line.getSections();
        List<String> stationNames = sections.stream()
                .map(section -> section.getUpwardStation().getName())
                .collect(Collectors.toList());
        stationNames.add(getDownwardTerminusName(sections));
        return new LineStationsResponse(line.getName(), stationNames);
    }

    private String getDownwardTerminusName(List<Section> sections) {
        return sections.get(sections.size() - 1).getDownwardStation().getName();
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
