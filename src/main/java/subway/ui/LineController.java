package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.ui.dto.StationInsertRequest;
import subway.service.LineService;
import subway.service.StationService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    public LineController(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody final LineRequest lineRequest) {
        final Long id = lineService.create(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> insertStation(@Valid @RequestBody final StationInsertRequest stationInsertRequest) {
        lineService.insertStation(stationInsertRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<Line> lines = lineService.findAll();
        final List<Station> stations = stationService.findAll();
        final Map<Long, Station> stationIdToStation = getIdToStation(stations);
        final List<LineResponse> lineResponses = getLineResponses(lines, stationIdToStation);
        return ResponseEntity.ok(lineResponses);
    }

    private Map<Long, Station> getIdToStation(final List<Station> stations) {
        return stations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    private List<LineResponse> getLineResponses(final List<Line> lines, final Map<Long, Station> stationIdToStation) {
        return lines.stream()
                .map(lineToLineResponse(stationIdToStation))
                .collect(Collectors.toList());
    }

    private Function<Line, LineResponse> lineToLineResponse(final Map<Long, Station> stationIdToStation) {
        return line -> {
            List<Station> stations = line.getStationIdsByOrder().stream()
                    .map(stationIdToStation::get)
                    .collect(Collectors.toList());
            return LineResponse.of(line, stations);
        };
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        final Line line = lineService.findLineById(id);
        final List<Station> stations = stationService.findById(line.getStationIdsByOrder());
        return ResponseEntity.ok(LineResponse.of(line, stations));
    }

    @DeleteMapping("/{lineId}/{stationId}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable(value = "lineId") final Long lineId,
            @PathVariable(value = "stationId") final Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
