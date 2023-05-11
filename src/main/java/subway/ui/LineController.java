package subway.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.SectionService;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineDetailResponse;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;
import subway.dto.section.SectionResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody LineCreateRequest lineRequest) {
        long lineId = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @GetMapping("/detail")
    public ResponseEntity<List<LineDetailResponse>> findAllDetailLines() {
        List<LineDetailResponse> lineDetailResponses = lineService.findLineResponses()
                .stream()
                .map(line -> new LineDetailResponse(line, sectionService.findSectionsByLineId(line.getLineId())))
                .collect(toList());
        return ResponseEntity.ok(lineDetailResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineDetailResponse> findLineDetailById(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.findLineResponseById(lineId);
        List<SectionResponse> sectionResponses = sectionService.findSectionsByLineId(lineId);
        return ResponseEntity.ok(new LineDetailResponse(lineResponse, sectionResponses));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId,
                                           @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }
}
