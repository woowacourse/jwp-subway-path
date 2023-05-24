package subway.line.presentation;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.LineCreateDto;
import subway.line.dto.LineResponseDto;
import subway.line.service.LineService;
import subway.section.dto.SectionCreateDto;
import subway.station.dto.StationResponseDto;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<List<LineResponseDto>> getLines() {
        return ResponseEntity.ok(lineService.findAllLines());
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponseDto>> getStations(@PathVariable final Long lineId) {
        return ResponseEntity.ok(lineService.findSortedStations(lineId));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody LineCreateDto lineCreateDto) {
        final Long lineId = lineService.createLine(lineCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> enrollStation(@RequestBody final SectionCreateDto sectionCreateDto) {
        final Long lineId = lineService.addSection(sectionCreateDto);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> disEnrollStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        lineService.removeStationBy(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
