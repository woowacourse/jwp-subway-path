package subway.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "create line property", description = "노선 정보 생성")
    public ResponseEntity<LinePropertyResponse> createLineProperty(@RequestBody LineRequest lineRequest) {
        LinePropertyResult result = linePropertyService.saveLineProperty(
                new SaveLinePropertyCommand(lineRequest.getName(), lineRequest.getColor()));
        LinePropertyResponse response = new LinePropertyResponse(result.getId(), result.getName(), result.getColor());

        return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
    }

    @GetMapping
    @Operation(summary = "find all line properties", description = "모든 노선 정보 반환")
    public ResponseEntity<List<LinePropertyResponse>> findAllLineProperties() {
        List<LinePropertyResult> results = linePropertyService.findAllLineProperty();
        List<LinePropertyResponse> responses = results.stream()
                .map(result -> new LinePropertyResponse(result.getId(), result.getName(), result.getColor()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "find line property by id", description = "노선 정보 반환")
    public ResponseEntity<LinePropertyResponse> findLinePropertyById(@PathVariable Long id) {
        LinePropertyResult result = linePropertyService.findLinePropertyById(new IdCommand(id));
        LinePropertyResponse response = new LinePropertyResponse(result.getId(), result.getName(), result.getColor());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update line property by id", description = "노선 정보 수정")
    public ResponseEntity<Void> updateLineProperty(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        linePropertyService.updateLineProperty(
                new UpdateLinePropertyCommand(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete line property by id", description = "노선 정보 삭제")
    public ResponseEntity<Void> deleteLineProperty(@PathVariable Long id) {
        linePropertyService.deleteLinePropertyById(new IdCommand(id));

        return ResponseEntity.noContent().build();
    }
}
