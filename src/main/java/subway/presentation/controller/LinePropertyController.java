package subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.core.service.LinePropertyService;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveLinePropertyCommand;
import subway.application.core.service.dto.in.UpdateLinePropertyCommand;
import subway.application.core.service.dto.out.LinePropertyResult;
import subway.presentation.dto.LineRequest;
import subway.presentation.dto.LinePropertyResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LinePropertyController {

    private final LinePropertyService linePropertyService;

    public LinePropertyController(LinePropertyService linePropertyService) {
        this.linePropertyService = linePropertyService;
    }

    @PostMapping
    public ResponseEntity<LinePropertyResponse> createLine(@RequestBody LineRequest lineRequest) {
        LinePropertyResult result = linePropertyService.saveLineProperty(
                new SaveLinePropertyCommand(lineRequest.getName(), lineRequest.getColor()));
        LinePropertyResponse response = new LinePropertyResponse(result.getId(), result.getName(), result.getColor());

        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LinePropertyResponse>> findAllLines() {
        List<LinePropertyResult> results = linePropertyService.findLinePropertyResponses();
        List<LinePropertyResponse> responses = results.stream()
                .map(result -> new LinePropertyResponse(result.getId(), result.getName(), result.getColor()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinePropertyResponse> findLineById(@PathVariable Long id) {
        LinePropertyResult result = linePropertyService.findLinePropertyResponseById(new IdCommand(id));
        LinePropertyResponse response = new LinePropertyResponse(result.getId(), result.getName(), result.getColor());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        linePropertyService.updateLineProperty(
                new UpdateLinePropertyCommand(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        linePropertyService.deleteLinePropertyById(new IdCommand(id));

        return ResponseEntity.noContent().build();
    }
}
