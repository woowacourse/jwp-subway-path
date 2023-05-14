package subway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.SectionCreateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.LinesResponse;
import subway.service.LineService;

@Tag(name = "Line", description = "노선 API Document")
@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @Operation(summary = "노선 추가 API", description = "새로운 노선을 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> createLine(@Valid @RequestBody LineCreateRequest request) {
        final Long lineId = lineService.createLine(request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @Operation(summary = "노선 정보 조회 API", description = "노선의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable(name = "id") Long lineId) {
        final LineResponse response = lineService.findLineById(lineId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 노선 정보 조회 API", description = "모든 노선의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<LinesResponse> findLines() {
        final LinesResponse response = lineService.findLines();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "노선 구간 등록 API", description = "노선에 새로운 구간을 등록합니다.")
    @PostMapping("/{id}/sections")
    public ResponseEntity<Void> createSection(
            @PathVariable(name = "id") Long lineId,
            @Valid @RequestBody SectionCreateRequest request
    ) {
        lineService.createSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @Operation(summary = "노선 구간 삭제 API", description = "노선의 특정 구간을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(
            @PathVariable(name = "id") Long lineId,
            @RequestParam Long stationId
    ) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
