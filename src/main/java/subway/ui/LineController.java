package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showAllLines() {
        final List<LineResponse> result = lineService.findLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> addLine(@RequestBody final LineRequest lineRequest) {
        final Long lineId = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping("/{id}/stations")
    public ResponseEntity<List<StationResponse>> showStations(@PathVariable final Long id) {
        final List<StationResponse> result = lineService.findAllStations(id).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> addStation(@PathVariable final Long id, @RequestBody final StationRequest stationRequest) {
        final Long stationId = lineService.addStation(id, stationRequest);

        return ResponseEntity.created(URI.create("/lines/" + id + "/stations/" + stationId)).build();
    }

    @DeleteMapping("/{line-id}/stations/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable("line-id") final Long lineId, @PathVariable final Long id) {
        lineService.deleteStation(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleLineNotFoundException(final LineNotFoundException lineNotFoundException) {
        return ResponseEntity.notFound().build();
    }
}
