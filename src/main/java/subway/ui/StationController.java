package subway.ui;

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
import subway.application.StationService;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.dto.StationUpdateRequest;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        stationService.saveStation(stationCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @PutMapping("/{stationName}")
    public ResponseEntity<Void> updateStation(@PathVariable String stationName,
                                              @RequestBody StationUpdateRequest stationUpdateRequest) {
        stationService.updateStation(stationName, stationUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{stationName}")
    public ResponseEntity<Void> deleteStation(@PathVariable String stationName) {
        stationService.deleteStationByName(stationName);
        return ResponseEntity.noContent().build();
    }
}
