package subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.repository.LineRepository;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long save(LineRequest lineRequest) {
        List<Section> sections = new ArrayList<>();
        return lineRepository.save(Line.of(null, lineRequest.getName(), sections));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), getStationResponse(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id);
        List<StationResponse> stationsResponse = getStationResponse(line);

        return new LineResponse(id, line.getName(), stationsResponse);
    }

    private List<StationResponse> getStationResponse(Line line) {
        return line.getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
