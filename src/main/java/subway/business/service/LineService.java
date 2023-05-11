package subway.business.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.business.domain.Direction;
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

    public void addStationToLine(long lineId, StationAddToLineRequest stationAddToLineRequest) {
        Line line = lineRepository.findById(lineId);
        line.addStation(
                stationAddToLineRequest.getStation(),
                stationAddToLineRequest.getNeighborhoodStation(),
                Direction.from(stationAddToLineRequest.getAddDirection()),
                stationAddToLineRequest.getDistance()
        );
    }

    public void deleteStation(Long lineId, String stationName) {
        Line line = lineRepository.findById(lineId);
        line.deleteStation(stationName);
        lineRepository.save(line);
    }

    public List<LineStationsResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::getLineStationsResponseFrom)
                .collect(Collectors.toList());
    }

    public LineStationsResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return getLineStationsResponseFrom(line);
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

    public LineResponse createLine(LineSaveRequest lineSaveRequest) {
        Line line = Line.of(
                lineSaveRequest.getName(),
                lineSaveRequest.getUpwardTerminus(),
                lineSaveRequest.getDownwardTerminus(),
                lineSaveRequest.getDistance()
        );
        return new LineResponse(lineRepository.create(line), line.getName());
    }
}
