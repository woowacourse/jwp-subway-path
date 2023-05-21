package subway.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.line.LineService;
import subway.application.section.SectionService;
import subway.dto.response.Response;
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
    public ResponseEntity<Response> createLine(@RequestBody @Valid LineCreateRequest lineRequest) {
        long lineId = lineService.saveLine(lineRequest);
        return Response.created(URI.create("/lines/" + lineId))
                .message("노선이 생성되었습니다.")
                .build();
    }

    @GetMapping
    public ResponseEntity<Response> findAllLines() {
        List<LineResponse> lines = lineService.findLineResponses();
        return Response.ok()
                .message(lines.size() + "개의 노선이 조회되었습니다.")
                .result(lines)
                .build();
    }

    @GetMapping("/detail")
    public ResponseEntity<Response> findAllDetailLines() {
        List<LineDetailResponse> detailLines = lineService.findLineResponses()
                .stream()
                .map(line -> new LineDetailResponse(line, sectionService.findSectionsByLineId(line.getLineId())))
                .collect(toList());
        return Response.ok()
                .message(detailLines.size() + "개의 상세 노선이 조회되었습니다.")
                .result(detailLines)
                .build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<Response> findLineDetailById(@PathVariable Long lineId) {
        LineResponse lineResponse = lineService.findLineResponseById(lineId);
        List<SectionResponse> sectionResponses = sectionService.findSectionsByLineId(lineId);
        return Response.ok()
                .message("상세 노선이 조회되었습니다.")
                .result(new LineDetailResponse(lineResponse, sectionResponses))
                .build();
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Response> updateLine(@PathVariable Long lineId,
                                               @RequestBody @Valid LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return Response.ok()
                .message("노선이 수정되었습니다.")
                .build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Response> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return Response.ok()
                .message("노선이 삭제되었습니다.")
                .build();
    }
}
