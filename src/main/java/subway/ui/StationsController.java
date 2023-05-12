package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.StationRequest;
import subway.dto.StationsSavingRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class StationsController {
    private final SectionService sectionService;

    public StationsController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> initializeStations(@PathVariable long lineId, @RequestBody StationsSavingRequest stationsSavingRequest) {
        long savedId = sectionService.insert(lineId, stationsSavingRequest.getPreviousStationName(),
                stationsSavingRequest.getNextStationName(), stationsSavingRequest.getDistance(), stationsSavingRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/stations")
    public ResponseEntity<Void> deleteStations(@PathVariable long lineId, @RequestBody StationRequest stationRequest) {
        sectionService.deleteStation(lineId, stationRequest.getName());
        return ResponseEntity.ok().build();
    }
}
