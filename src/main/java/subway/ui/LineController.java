package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.business.service.LineService;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;
import subway.ui.dto.StationDeleteRequest;

@Tag(name = "Line", description = "노선 API Document")
@RequestMapping("/lines")
@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @Operation(summary = "노선 등록", description = "상행 종점, 하행 종점역을 가지는 노선을 등록합니다.")
    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineSaveRequest lineSaveRequest) {
        LineResponse lineResponse = lineService.createLine(lineSaveRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineResponse.getId()))
                .body(lineResponse);
    }

    @Operation(summary = "노선에 역 추가", description = "노선 사이에 역을 추가합니다. 기준 역과, 추가될 방향을 지정해야 합니다.")
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<LineResponse> addStationToLine(
            @NonNull @PathVariable Long lineId,
            @RequestBody StationAddToLineRequest stationAddToLineRequest
    ) {
        LineResponse lineResponse = lineService.addStationToLine(lineId, stationAddToLineRequest);
        return ResponseEntity.ok(lineResponse);
    }

    @Operation(summary = "노선에서 역 제거", description = "노선의 역을 제거합니다.")
    @DeleteMapping("/{lineId}/stations")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId,
                                              @RequestBody StationDeleteRequest stationDeleteRequest) {
        lineService.deleteStation(lineId, stationDeleteRequest.getStation());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 노선 조회", description = "특정 노선과 그에 포함된 모든 구간의 정보를 조회합니다.")
    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineResponseById(lineId));
    }

    @Operation(summary = "모든 노선 조회", description = "모든 노선과 그에 포함된 모든 구간의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }
}
