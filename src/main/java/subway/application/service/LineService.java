package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.Distance;
import subway.application.domain.Line;
import subway.application.domain.Section;
import subway.application.repository.LinePropertyRepository;
import subway.application.repository.LineRepository;
import subway.application.repository.StationRepository;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final LinePropertyRepository linePropertyRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, LinePropertyRepository linePropertyRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.linePropertyRepository = linePropertyRepository;
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
        lineRepository.removeSections(lineId);
        lineRepository.save(line);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId);

        line.deleteStation(stationRepository.findById(stationId));
        lineRepository.removeSections(lineId);
        lineRepository.save(line);
    }

    public List<StationResponse> findRouteMap(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return line.routeMap().value()
                .stream()
                .map(station -> new StationResponse(
                        station.getId(),
                        station.getName()
                )).collect(Collectors.toList());
    }

    public Map<String, List<StationResponse>> findAllRouteMap() {
        List<Line> allLines = linePropertyRepository.findAll().stream()
                .map(line -> lineRepository.findById(line.getId()))
                .collect(Collectors.toList());
        Map<String, List<StationResponse>> allRouteMap = new HashMap<>();
        for (Line line : allLines) {
            allRouteMap.put(line.getName(), findRouteMap(line.getId()));
        }
        return allRouteMap;
    }
}
