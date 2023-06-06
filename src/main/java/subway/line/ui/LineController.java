package subway.line.ui;

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
import subway.line.application.LineCommandService;
import subway.line.application.LineQueryService;
import subway.line.dto.request.LineAddStationRequest;
import subway.line.dto.request.LineCreateRequest;
import subway.line.dto.request.LineUpdateInfoRequest;
import subway.line.dto.response.LineResponse;
import subway.line.dto.response.LinesResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineCommandService lineCommandService;
    private final LineQueryService lineQueryService;

    public LineController(LineCommandService lineCommandService, LineQueryService lineQueryService) {
        this.lineCommandService = lineCommandService;
        this.lineQueryService = lineQueryService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineCreateRequest request) {
        LineResponse responseDto = lineCommandService.createLine(request);
        return ResponseEntity
                .created(URI.create("/lines/" + responseDto.getId()))
                .body(responseDto);
    }

    @PostMapping("/{id}")
    public ResponseEntity<LineResponse> addLineStation(@PathVariable Long id,
            @RequestBody LineAddStationRequest request) {
        LineResponse responseDto = lineCommandService.addInterStation(id, request);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long id) {
        lineCommandService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<LinesResponse> findAllLines() {
        List<LineResponse> resultDtos = lineQueryService.findAllLines();
        return ResponseEntity.ok(new LinesResponse(resultDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findById(@PathVariable Long id) {
        LineResponse resultDto = lineQueryService.findById(id);
        return ResponseEntity.ok(resultDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLineInfo(@PathVariable Long id,
            @RequestBody @Valid LineUpdateInfoRequest request) {
        lineCommandService.updateLine(id, request);
        return ResponseEntity.noContent().build();
    }
}
