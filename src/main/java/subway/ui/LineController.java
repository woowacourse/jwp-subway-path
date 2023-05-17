package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

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

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody final LineRequest lineRequest) {
        final LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PostMapping("/{id}/station/init")
    public ResponseEntity<SectionResponse> createInitialSection(@PathVariable final Long id,
                                                                @RequestBody final SectionRequest sectionRequest) {
        final SectionResponse section = lineService.saveInitialSection(id, sectionRequest);
        final URI uri = URI.create("/lines/" + id + "/station/init/" + section.getId());
        return ResponseEntity.created(uri).body(section);
    }
//
//    @PostMapping("/{id}/station")
//    public ResponseEntity<SectionResponse> createSection(@PathVariable final Long id,
//                                                         @RequestBody final SectionRequest sectionRequest) {
//        final SectionResponse section = lineService.saveSection(id, sectionRequest);
//        final URI uri = URI.create("/lines/" + id + "/station/" + section.getId());
//        return ResponseEntity.created(uri).body(section);
//    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id, @RequestBody final LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/station/{stationId}")
    public ResponseEntity<Void> deleteStationInLine(@PathVariable final Long id,
                                                    @PathVariable final Long stationId) {
        lineService.deleteSectionByStationId(id, stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException(final Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
