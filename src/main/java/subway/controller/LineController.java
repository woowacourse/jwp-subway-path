package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;
import subway.service.LineService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineResponse> response = lineService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<LineResponse> createNewLine(@Valid @RequestBody LineCreateRequest request) {
        LineResponse response = lineService.createNewLine(request);

        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @PostMapping("/{id}/station-init")
    public ResponseEntity<LineResponse> createInitialSectionInLine(
            @PathVariable("id") Long id,
            @Valid @RequestBody InitialSectionCreateRequest request) {
        final LineResponse lineResponse = lineService.createInitialSection(id, request);
        return ResponseEntity
                .created(URI.create("/" + lineResponse.getId() + "/stations"))
                .body(lineResponse);
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<LineResponse> addStationInLine(
            @PathVariable("id") Long id,
            @Valid @RequestBody SectionCreateRequest request) {

        LineResponse response = lineService.addStation(id, request);

        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStationInLine(
            @PathVariable("lineId") Long lineId,
            @PathVariable("stationId") Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
