package subway.presentation;

import java.util.stream.Collectors;
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
import subway.application.dto.ReadLineDto;
import subway.presentation.dto.request.CreateLineRequest;
import subway.presentation.dto.response.CreateLineResponse;
import subway.presentation.dto.response.ReadLineResponse;

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
    public ResponseEntity<CreateLineResponse> createLine(@RequestBody final CreateLineRequest lineRequest) {
        final CreationLineDto lineDto = lineService.saveLine(lineRequest.getName(), lineRequest.getColor());

        return ResponseEntity.created(URI.create("/lines/" + lineDto.getId())).body(CreateLineResponse.from(lineDto));
    }

    @GetMapping
    public ResponseEntity<List<ReadLineResponse>> findAllLines() {
        final List<ReadLineResponse> responses = lineService.findAllLine().stream()
                .map(ReadLineResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadLineResponse> findLineById(@PathVariable final Long id) {
        final ReadLineDto dto = lineService.findLineById(id);

        return ResponseEntity.ok(ReadLineResponse.from(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.deleteLineById(id);

        return ResponseEntity.noContent().build();
    }
}
