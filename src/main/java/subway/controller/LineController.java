package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LineStationsResponse;
import subway.service.LineService;
import subway.service.SectionService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineStationsResponse>> findAllLines() {
        List<LineStationsResponse> lineStationsResponses = sectionService.readAllStationsOfAllLines();
        return ResponseEntity.ok().body(lineStationsResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineStationsResponse> findLineById(@PathVariable Long id) {
        LineStationsResponse lineStationsResponse = sectionService.readAllStationsOfLine(id);
        return ResponseEntity.ok().body(lineStationsResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @Valid @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }


}
