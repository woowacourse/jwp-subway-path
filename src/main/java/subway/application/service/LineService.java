package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.Distance;
import subway.application.domain.Line;
import subway.application.domain.Section;
import subway.application.repository.LineRepository;
import subway.application.repository.StationRepository;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void enrollStation(Long lineId, StationEnrollRequest request) {
        Line line = lineRepository.findById(lineId);
        Section section = new Section(
                stationRepository.findById(request.getUpBound()),
                stationRepository.findById(request.getDownBound()),
                new Distance(request.getDistance())
        );
        line.addSection(section);
        lineRepository.insert(line);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId);

        line.deleteStation(stationRepository.findById(stationId));
        lineRepository.insert(line);
    }

    public List<StationResponse> findRouteMap(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return makeRouteMapResponseOf(line);
    }

    public Map<String, List<StationResponse>> findAllRouteMap() {
        List<Line> allLines = lineRepository.findAll();

        return allLines.stream()
                .collect(Collectors.toMap(Line::getName, LineService::makeRouteMapResponseOf));
    }

    private static List<StationResponse> makeRouteMapResponseOf(Line line) {
        return line.routeMap().value().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
