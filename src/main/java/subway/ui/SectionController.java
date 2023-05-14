package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.StationRequest;
import subway.dto.SectionSavingRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/section")
    public ResponseEntity<Void> initializeSection(@PathVariable long lineId, @RequestBody SectionSavingRequest sectionSavingRequest) {
        long savedId = sectionService.insert(lineId, sectionSavingRequest.getPreviousStationName(),
                sectionSavingRequest.getNextStationName(), sectionSavingRequest.getDistance(), sectionSavingRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/section")
    public ResponseEntity<Void> deleteSection(@PathVariable long lineId, @RequestBody StationRequest stationRequest) {
        sectionService.deleteStation(lineId, stationRequest.getName());
        return ResponseEntity.ok().build();
    }
}
