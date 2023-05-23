package subway.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.application.SectionService;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.StationsByLineDto;
import subway.dto.StationsByLineResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<StationsByLineResponse>> findAllLines() {
        List<Line> lines = lineService.findLines();
        List<StationsByLineResponse> stationsByLineResponses = new ArrayList<>();
        for (Line targetLine : lines) {
            StationsByLineDto stationsByLineDto = sectionService.showStations(targetLine);
            stationsByLineResponses.add(new StationsByLineResponse(
                    stationsByLineDto.getLineResponse().getId(),
                    stationsByLineDto.getLineResponse().getName(),
                    stationsByLineDto.getLineResponse().getColor(),
                    stationsByLineDto.getStations())
            );
        }
        return ResponseEntity.ok(stationsByLineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<StationsByLineResponse> findLineById(@PathVariable Long lineId) {
        Line targetLine = lineService.findLineById(lineId);
        StationsByLineDto stationsByLineDto = sectionService.showStations(targetLine);
        return ResponseEntity.ok(new StationsByLineResponse(
                stationsByLineDto.getLineResponse().getId(),
                stationsByLineDto.getLineResponse().getName(),
                stationsByLineDto.getLineResponse().getColor(),
                stationsByLineDto.getStations()
        ));
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<Void> addStationAtLine(@PathVariable Long lineId,
                                                 @RequestBody SectionRequest sectionRequest) {
        sectionService.addSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteStationAtLine(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.deleteStationById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

}
