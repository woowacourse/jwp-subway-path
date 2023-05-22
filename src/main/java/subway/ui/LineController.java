package subway.ui;

import java.net.URI;
import java.util.List;
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
import subway.application.RoutingService;
import subway.application.SectionService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.dto.StationResponse;
import subway.dto.StationToLineRequest;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;
    private final RoutingService routingService;

    public LineController(LineService lineService, SectionService sectionService, final RoutingService routingService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
        this.routingService = routingService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLineResponses());
    }

    @PostMapping("/{id}")
    public ResponseEntity<SectionResponse> registerStation(@PathVariable Long id,
                                                           @RequestBody StationToLineRequest stationToLineRequest) {
        SectionResponse section = sectionService.connectStation(id, stationToLineRequest);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(section);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stations")
    public ResponseEntity<List<StationResponse>> findAllStationByLineId(@PathVariable Long id) {
        return ResponseEntity.ok(routingService.findStationResponses(id));
    }

    @DeleteMapping("/{id}/stations")
    public ResponseEntity<Void> deleteAllSectionByLineId(@PathVariable Long id) {
        sectionService.deleteAllByLineId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id, @PathVariable Long stationId) {
        sectionService.disconnectStation(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
