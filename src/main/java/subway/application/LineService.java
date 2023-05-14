package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.H2LinePropertyRepository;
import subway.dao.H2StationRepository;
import subway.dao.H2LineRepository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.StationEnrollRequest;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final H2LineRepository lineRepository;
    private final H2LinePropertyRepository linePropertyRepository;
    private final H2StationRepository stationRepository;

    public LineService(H2LineRepository lineRepository, H2LinePropertyRepository linePropertyRepository, H2StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.linePropertyRepository = linePropertyRepository;
        this.stationRepository = stationRepository;
    }

    public void enrollStation(Long lineId, StationEnrollRequest request) {
        Line line = lineRepository.findById(lineId);
        Section section = new Section(
                stationRepository.findById(request.getUpStation()),
                stationRepository.findById(request.getDownStation()),
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
        return line.routeMap()
                .getRouteMap()
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
