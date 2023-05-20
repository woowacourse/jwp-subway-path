package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineStationResponse;
import subway.dto.LineStationRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineRequest lineRequest) {
        Long lineId = lineService.save(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> registerStation(
            @PathVariable final Long id,
            @RequestBody final LineStationRequest lineStationRequest
    ) {
        lineService.saveStationInLine(id, lineStationRequest);
        return ResponseEntity.created(URI.create("/lines/" + id + "/stations/")).build();
    }

    @GetMapping
    public ResponseEntity<List<LineStationResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineStationResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @Valid @RequestBody LineRequest lineUpdateRequest) {
        lineService.update(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable final Long id, @PathVariable final Long stationId) {
        lineService.deleteByLineIdAndStationId(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
