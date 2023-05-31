package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.domain.Line;
import subway.dto.AddLineRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final SubwayService subwayService;

    public LineController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(@RequestBody AddLineRequest addLineRequest) {
        long lineId = subwayService.addNewLine(addLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(@RequestBody DeleteStationRequest deleteStationRequest) {
        subwayService.deleteStation(deleteStationRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        Line line = subwayService.findLineById(id);
        return ResponseEntity.ok(toLineResponse(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<Line> lines = subwayService.findAllLines();
        List<LineResponse> lineResponses = lines.stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lineResponses);
    }

    private LineResponse toLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getStationNamesInOrder(), line.getName().getName());
    }
}
