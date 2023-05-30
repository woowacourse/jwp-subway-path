package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Lines", description = "노선 API")
public class LineController {

    private final LineQueryService lineQueryService;
    private final LineCommandService lineCommandService;

    public LineController(final LineQueryService lineQueryService, final LineCommandService lineCommandService) {
        this.lineQueryService = lineQueryService;
        this.lineCommandService = lineCommandService;
    }

    @PostMapping
    @Operation(summary = "노선 생성 API", description = "새로운 노선을 생성합니다.")
    public ResponseEntity<CreationLineResponse> createLine(@Valid @RequestBody final CreationLineRequest request) {
        final Line line = lineCommandService.saveLine(request.getName(), request.getColor());
        final CreationLineResponse response = CreationLineResponse.from(line);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(response);
    }

    @GetMapping
    @Operation(summary = "모든 노선 조회 API", description = "저장된 모든 노선의 정보를 조회합니다.")
    public ResponseEntity<List<ReadLineResponse>> findAllLines() {
        final List<Line> lines = lineQueryService.findAllLine();

        final List<ReadLineResponse> responses = lines.stream()
                .map(ReadLineResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{lineId}")
    @Operation(summary = "특정 노선 조회 API", description = "저장된 특정 노선의 정보를 조회합니다.")
    public ResponseEntity<ReadLineResponse> findLineById(@PathVariable final Long lineId) {
        final Line line = lineQueryService.findLineById(lineId);
        final ReadLineResponse response = ReadLineResponse.of(line);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "노선 삭제 API", description = "저장된 노선을 삭제합니다.")
    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long lineId) {
        lineCommandService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    @Operation(summary = "노선 구간 추가 API", description = "노선에 구간을 생성합니다.")
    public ResponseEntity<Void> postAddSection(@PathVariable final Long lineId,
                                               @Valid @RequestBody final CreationSectionRequest request) {
        lineCommandService.saveSection(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    @Operation(summary = "노선 역 삭제 API", description = "노선에 저장된 역을 삭제합니다.")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId,
                                              @PathVariable final Long stationId) {
        lineCommandService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}


