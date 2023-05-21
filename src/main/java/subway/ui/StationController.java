package subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationRemoveService;
import subway.application.StationSaveService;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationSaveService stationSaveService;
    private final StationRemoveService stationRemoveService;

    public StationController(final StationSaveService stationSaveService, StationRemoveService stationRemoveService) {
        this.stationSaveService = stationSaveService;
        this.stationRemoveService = stationRemoveService;
    }

    @PostMapping
    public ResponseEntity<StationSaveResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationSaveResponse stationSaveResponse = stationSaveService.saveStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/lines/" + stationSaveResponse.getLine().getId()))
                .body(stationSaveResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStation(@PathVariable Long id) {
        stationRemoveService.removeStationById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
