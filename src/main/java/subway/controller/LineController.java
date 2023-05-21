package subway.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineAddRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.service.LineService;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody @Valid final LineAddRequest request) {
        final Long id = lineService.add(request);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAll() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable final Long id,
            @RequestBody @Valid final LineUpdateRequest request
    ) {
        lineService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        lineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
