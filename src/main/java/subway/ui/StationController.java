package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }


    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody final StationRequest stationRequest) {
        StationResponse stationResponse = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + stationResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<StationResponse> showStationsByLineId(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable final Long id, @RequestBody final StationRequest stationRequest) {
        stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }

}
