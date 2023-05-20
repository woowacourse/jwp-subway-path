package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.StationService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final StationService stationService;

    public LineController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    /**
     * 노선 추가
     *
     * @param lineRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createLine(@RequestBody LineRequest lineRequest) {
        Long newLineId = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + newLineId)).body(newLineId);
    }

    /**
     * 노선도에 있는 모든 노선들의 정보를 조회
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    /**
     * 특정 노선의 정보 조회
     *
     * @param lineId
     * @return
     */
    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.findLineResponseById(lineId));
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 노선의 역 목록 조회
     *
     * @param lineId
     * @return
     */
    @GetMapping("/{lineId}/stations")
    public ResponseEntity<List<StationResponse>> findLineStationsById(@PathVariable Long lineId) {
        return ResponseEntity.ok(stationService.findLineStationResponsesById(lineId));
    }

    /**
     * 노선에 역 추가
     *
     * @param lineId
     * @param request
     * @return
     */
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Long> createStation(@PathVariable Long lineId,
        @RequestBody StationRequest request) {
        Long newStationId = stationService.saveStation(lineId, request);
        return ResponseEntity.created(URI.create("/stations/" + newStationId)).body(newStationId);
    }

    /**
     * 특정 노선의 역 제거
     *
     * @param name
     * @param lineId
     * @return
     */
    @DeleteMapping("/{lineId}/stations")
    public ResponseEntity<List<StationResponse>> deleteStation(@RequestBody String name,
        @PathVariable Long lineId) {
        stationService.deleteStation(lineId, name);
        return ResponseEntity.noContent().build();
    }
}
