package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SubwayService;
import subway.dto.AddLineRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final SubwayService subwayService;

    public LineController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody AddLineRequest addLineRequest) {
        Long lineId = subwayService.addLine(addLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(@RequestBody DeleteStationRequest deleteStationRequest) {
        subwayService.deleteStation(deleteStationRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(subwayService.findLineById(id));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(subwayService.findAllLines());
    }
}
