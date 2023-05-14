package subway.ui;

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
import subway.dto.AddStationToLineRequest;
import subway.dto.AddStationToLineResponse;
import subway.dto.DeleteStationFromLineResponse;
import subway.dto.GetAllStationsInLineResponse;
import subway.dto.GetAllStationsInLineResponses;
import subway.dto.LineCreateRequest;
import subway.service.LineService;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineCreateRequest createRequest) {
        final Line newLine = lineService.createNewLine(createRequest);
        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<AddStationToLineResponse> addStationToLine(@PathVariable Long lineId,
                                                                     @RequestBody AddStationToLineRequest addStationToLineRequest) {
        Line line = lineService.addStationToExistLine(lineId, addStationToLineRequest);
        List<Long> stationIds = line.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AddStationToLineResponse(line.getId(), line.getName(), stationIds));
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<GetAllStationsInLineResponse> findLineById(@PathVariable Long lineId) {
        final Line line = lineService.getLine(lineId);
        final List<Station> stations = lineService.getStations(lineId);
        final GetAllStationsInLineResponse response = new GetAllStationsInLineResponse(line, stations);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GetAllStationsInLineResponses> findAllLines() {
        final Map<Line, List<Station>> result = lineService.findAllLine2();

        return ResponseEntity.ok(new GetAllStationsInLineResponses(result));
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<DeleteStationFromLineResponse> delete(@PathVariable Long lineId,
                                                                @PathVariable Long stationId) {
        Line line = lineService.deleteStationFromLine(lineId, stationId);
        List<Long> stationIds = line.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new DeleteStationFromLineResponse(line.getId(), line.getName(), stationIds));
    }
}