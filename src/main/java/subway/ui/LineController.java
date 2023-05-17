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
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    /**
     * 노선 추가
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

}
