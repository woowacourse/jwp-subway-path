package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.DeleteStationService;
import subway.application.SaveSectionService;
import subway.dto.StationRequest;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final SaveSectionService saveSectionService;
    private final DeleteStationService deleteStationService;

    public StationController(SaveSectionService saveSectionService, DeleteStationService deleteStationService) {
        this.saveSectionService = saveSectionService;
        this.deleteStationService = deleteStationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody StationRequest stationRequest) {
        Long lineId = saveSectionService.saveSection(stationRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        Long lineId = deleteStationService.deleteStationById(id);
        return ResponseEntity
                .noContent()
                .location(URI.create("/lines/" + lineId))
                .build();
    }
}
