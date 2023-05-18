package subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.service.LinePropertyService;
import subway.application.service.command.in.IdCommand;
import subway.application.service.command.in.SaveLinePropertyCommand;
import subway.application.service.command.in.UpdateLinePropertyCommand;
import subway.presentation.dto.LineRequest;
import subway.presentation.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LinePropertyController {

    private final LinePropertyService linePropertyService;

    public LinePropertyController(LinePropertyService linePropertyService) {
        this.linePropertyService = linePropertyService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = linePropertyService.saveLineProperty(
                new SaveLinePropertyCommand(lineRequest.getName(), lineRequest.getColor()));
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(linePropertyService.findLinePropertyResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(linePropertyService.findLinePropertyResponseById(new IdCommand(id)));
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
