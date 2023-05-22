package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.LineService;
import subway.service.dto.response.LineResponse;
import subway.service.dto.request.RegisterLineRequest;

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
    public ResponseEntity<List<LineResponse>> searchAllLines() {
        return ResponseEntity.ok(lineService.searchAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> searchLine(@PathVariable long id) {
        return ResponseEntity.ok(lineService.searchLine(id));
    }

    @PostMapping
    public ResponseEntity<Void> registerLine(@RequestBody RegisterLineRequest registerLineRequest) {
        long lineId = lineService.registerLine(registerLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
