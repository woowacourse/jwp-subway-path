package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineCommandService;
import subway.application.LineQueryService;
import subway.domain.Line;
import subway.ui.dto.request.CreationLineRequest;
import subway.ui.dto.request.CreationSectionRequest;
import subway.ui.dto.response.CreationLineResponse;
import subway.ui.dto.response.ReadLineResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineQueryService lineQueryService;
    private final LineCommandService lineCommandService;

    public LineController(final LineQueryService lineQueryService, final LineCommandService lineCommandService) {
        this.lineQueryService = lineQueryService;
        this.lineCommandService = lineCommandService;
    }

    @PostMapping
    public ResponseEntity<CreationLineResponse> createLine(@Valid @RequestBody final CreationLineRequest request) {
        final Line line = lineCommandService.saveLine(request.getName(), request.getColor());
        final CreationLineResponse response = CreationLineResponse.from(line);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadLineResponse>> findAllLines() {
        final List<Line> lines = lineQueryService.findAllLine();

        final List<ReadLineResponse> responses = lines.stream()
                .map(ReadLineResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<ReadLineResponse> findLineById(@PathVariable final Long lineId) {
        final Line line = lineQueryService.findLineById(lineId);
        final ReadLineResponse response = ReadLineResponse.of(line);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {
        lineCommandService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> postAddSection(@PathVariable final Long lineId,
                                               @Valid @RequestBody final CreationSectionRequest request) {
        lineCommandService.saveSection(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId,
                                              @PathVariable final Long stationId) {
        lineCommandService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
