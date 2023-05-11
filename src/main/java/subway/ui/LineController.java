package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.dto.CreationLineDto;
import subway.ui.dto.request.CreationLineRequest;
import subway.ui.dto.response.CreationLineResponse;
import subway.ui.dto.response.ReadLineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<CreationLineResponse> createLine(@RequestBody final CreationLineRequest lineRequest) {
        final CreationLineDto lineDto = lineService.saveLine(lineRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineDto.getId())).body(CreationLineResponse.from(lineDto));
    }

    @GetMapping
    public ResponseEntity<List<ReadLineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLine());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLineResponse> findLineById(@PathVariable final Long id) {
        return ResponseEntity.ok(lineService.findLineById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);

        return ResponseEntity.noContent().build();
    }
}
