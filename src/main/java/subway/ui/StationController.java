package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.StationRequest;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    // TODO : ControllerAdvice 구현
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody StationRequest stationRequest) {
        Long lineId = stationService.saveSection(stationRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + lineId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        Long lineId = stationService.deleteStationById(id);
        return ResponseEntity
                .noContent()
                .location(URI.create("/lines/" + lineId))
                .build();
    }
}
