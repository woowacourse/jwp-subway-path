package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.Direction;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.domain.Section;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.LineStationsResponse;
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
    public List<LineStationsResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::getLineStationsResponseFrom)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineStationsResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return getLineStationsResponseFrom(line);
    }

    public LineResponse createLine(LineSaveRequest lineSaveRequest) {
        Line line = Line.of(
                lineSaveRequest.getName(),
                lineSaveRequest.getUpwardTerminus(),
                lineSaveRequest.getDownwardTerminus(),
                lineSaveRequest.getDistance()
        );
        return new LineResponse(lineRepository.create(line), line.getName());
    }

    private LineStationsResponse getLineStationsResponseFrom(Line line) {
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
}
