package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.Distance;
import subway.application.domain.Line;
import subway.application.domain.Section;
import subway.application.repository.LineRepository;
import subway.application.repository.StationRepository;
import subway.application.service.dto.in.DeleteStationCommand;
import subway.application.service.dto.in.EnrollStationCommand;
import subway.application.service.dto.in.IdCommand;
import subway.application.service.dto.out.StationResult;

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

    public void enrollStation(EnrollStationCommand command) {
        Line line = lineRepository.findById(command.getLineId());
        Section section = new Section(
                stationRepository.findById(command.getUpBound()),
                stationRepository.findById(command.getDownBound()),
                new Distance(command.getDistance())
        );
        line.addSection(section);
        lineRepository.insert(line);
    }

    public void deleteStation(DeleteStationCommand command) {
        Line line = lineRepository.findById(command.getLineId());
        line.deleteStation(stationRepository.findById(command.getStationId()));
        lineRepository.insert(line);
    }

    public List<StationResult> findRouteMap(IdCommand command) {
        Line line = lineRepository.findById(command.getId());
        return makeStationResultsOf(line);
    }

    public Map<String, List<StationResult>> findAllRouteMap() {
        List<Line> allLines = lineRepository.findAll();
        return allLines.stream()
                .collect(Collectors.toMap(Line::getName, this::makeStationResultsOf));
    }

    private List<StationResult> makeStationResultsOf(Line line) {
        return line.routeMap().value().stream()
                .map(station -> new StationResult(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
