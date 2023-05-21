package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.service.section.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> addStation(@PathVariable Long lineId, @RequestBody @Valid LineStationRequest lineStationRequest) {
        Long id = sectionService.addStation(lineId, lineStationRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<LineStationResponse> getLineStations(@PathVariable Long lineId) {
        LineStationResponse lineStationResponse = sectionService.findByLineId(lineId);
        return ResponseEntity.ok().body(lineStationResponse);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.removeStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
