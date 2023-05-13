package subway.ui;

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
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;
import subway.dto.StationsResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    /**
     * 노선을 생성한다.
     * @param lineRequest (lineName, sourceStation, targetStation, distance)
     * @return 생성한 Line Id
     */
    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    /**
     * 한 노선의 정보를 조회한다.
     * @param lineId
     * @return StationsResponse - 노선 내에 존재하는 모든 역의 이름 목록
     */
    @GetMapping("/{lineId}")
    public ResponseEntity<StationsResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getStationsByLineId(lineId));
    }

    /**
     * 노선에 역을 추가한다.
     * @param lineId
     * @param stationSaveRequest (SourceStation, TargetStation, Distance)
     * @return StationResponse 추가한 역의 이름
     */
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<StationResponse> createStation(@PathVariable Long lineId,
                                                         @RequestBody StationSaveRequest stationSaveRequest) {
        StationResponse response = lineService.addStation(lineId, stationSaveRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 노선을 여러개 생성한다.
     * @return 생성한 노선들의 Id 목록
     */
    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    /**
     * (요구사항 X) 한 노선의 정보를 수정한다.
     * @param lineId
     * @param lineUpdateRequest -> Line 수정에 어떤 정보가 필요할지 더 고민해보고 나중에 결정
     */
    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(lineId, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 노선을 삭제한다.
     * @param lineId
     */
    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }
}
