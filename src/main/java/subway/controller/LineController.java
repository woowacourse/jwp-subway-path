package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationCreateRequest;
import subway.service.LineService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineCreateRequest request) {
        LineResponse response = lineService.createLineWithoutStation(request);

        return ResponseEntity
                .created(URI.create("/lines/" + response.getId()))
                .body(response);
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<LineResponse> createStationInLine(
            @PathVariable("id") Long id,
            @Valid @RequestBody StationCreateRequest request) {
        LineResponse response = lineService.addStation(id, request);
        final URI location = URI.create("/lines/" + response.getId());

        return ResponseEntity
                .created(location)
                .body(response);
    }
}
