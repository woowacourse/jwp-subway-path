package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

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
        StationSaveResponse stationSaveResponse = stationService.saveStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + stationSaveResponse.getLineId()))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
