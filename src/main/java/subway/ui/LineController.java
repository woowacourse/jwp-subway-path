package subway.ui;

import static java.util.stream.Collectors.toMap;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationInsertRequest;
import subway.service.LineService;
import subway.service.StationService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    public LineController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineRequest lineRequest) {
        Long id = lineService.create(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> insertStation(@RequestBody StationInsertRequest stationInsertRequest) {
        lineService.insertStation(stationInsertRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<Line> lines = lineService.findAll();
        List<Station> stations = stationService.findAll();
        Map<Long, Station> stationIdToStation = getIdToStation(stations);
        List<LineResponse> lineResponses = getLineResponses(lines, stationIdToStation);
        return ResponseEntity.ok(lineResponses);
    }

    private Map<Long, Station> getIdToStation(List<Station> stations) {
        return stations.stream()
                .collect(toMap(station -> station.getId(), Function.identity()));
    }

    private List<LineResponse> getLineResponses(List<Line> lines, Map<Long, Station> stationIdToStation) {
        List<LineResponse> lineResponses = lines.stream()
                .map(line -> toResponse(line, stationIdToStation))
                .collect(Collectors.toList());
        return lineResponses;
    }

    private LineResponse toResponse(Line line, Map<Long, Station> stationIdToStation) {
        List<Station> stations = line.getStationEdges().stream()
                .map(stationEdge -> {
                    Long downStationId = stationEdge.getDownStationId();
                    return stationIdToStation.get(downStationId);
                })
                .collect(Collectors.toList());
        return LineResponse.of(line, stations);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        Line line = lineService.findLineById(id);
        List<Station> stations = stationService.findById(line.getStationIds());
        return ResponseEntity.ok(LineResponse.of(line, stations));
    }

    @DeleteMapping("/{lineId}/{stationId}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable(value = "lineId") Long lineId,
            @PathVariable(value = "stationId") Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
