package subway.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSelectResponse;
import subway.dto.LinesSelectResponse;
import subway.dto.StationSaveRequest;
import subway.dto.StationSelectResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    private LineController(LineService lineService) {
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
    public ResponseEntity<LineSelectResponse> findLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getStationsByLineId(lineId));
    }

    /**
     * 노선에 역을 추가한다.
     * @param lineId
     * @param stationSaveRequest (SourceStation, TargetStation, Distance)
     * @return StationResponse 추가한 역의 이름
     */
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<StationSelectResponse> createStation(@PathVariable Long lineId,
                                                               @RequestBody StationSaveRequest stationSaveRequest) {
        StationSelectResponse response = lineService.addStation(lineId, stationSaveRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
    /**
     * 노선을 여러개 생성한다.
     * @return 생성한 노선들의 Id 목록
     */
    @GetMapping
    public ResponseEntity<LinesSelectResponse> findAllLines() {
        return ResponseEntity.ok(lineService.findAllLine());
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
