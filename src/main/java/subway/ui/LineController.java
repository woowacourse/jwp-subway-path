package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.service.SubwayService;
import subway.service.dto.LineDto;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.ui.dto.StationInsertRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final SubwayService subwayService;

    public LineController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody final LineRequest lineRequest) {
        final Long id = subwayService.create(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).build();
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> insertStation(@Valid @RequestBody final StationInsertRequest stationInsertRequest) {
        subwayService.insertStation(stationInsertRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineDto> lines = subwayService.findAll();
        final List<LineResponse> lineResponses = lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable final Long id) {
        final LineDto lineDto = subwayService.findLineById(id);
        return ResponseEntity.ok(LineResponse.of(lineDto));
    }

    @DeleteMapping("/{lineId}/{stationId}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable(value = "lineId") final Long lineId,
            @PathVariable(value = "stationId") final Long stationId) {
        subwayService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
