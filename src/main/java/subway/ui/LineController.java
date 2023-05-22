package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.domain.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.dto.StationEnrollRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines(@RequestParam(defaultValue = "false") final boolean withStation) {
        if (withStation) {
            return ResponseEntity.ok(LineResponse.of(lineService.findAllRouteMap()));
        }
        return ResponseEntity.ok(LineResponse.of(lineService.findLines()));
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(
            @PathVariable final Long lineId,
            @RequestParam(defaultValue = "false") final boolean withStation
    ) {
        if (withStation) {
            return ResponseEntity.ok(LineResponse.of(lineService.findLineWithStationsById(lineId)));
        }
        return ResponseEntity.ok(LineResponse.of(lineService.findLineById(lineId)));
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse line = LineResponse.of(lineService.saveLine(lineRequest));
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PostMapping("{lineId}/stations")
    public ResponseEntity<SectionResponse> enrollStation(@PathVariable final Long lineId,
                                                         @RequestBody final StationEnrollRequest request) {
        final Section section = lineService.enrollStation(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(new SectionResponse(lineId, section.getLeft(), section.getRight()));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable final Long lineId, @RequestBody final LineRequest lineUpdateRequest) {
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(LineResponse.of(lineService.updateLine(lineId, lineUpdateRequest)));
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", "/line/" + lineId).build();
    }


    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
