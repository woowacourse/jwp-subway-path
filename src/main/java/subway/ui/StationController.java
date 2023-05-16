package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public final class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid final StationRequest stationRequest) {
        final StationResponse station = stationService.saveStation(stationRequest);

        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findStationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable final Long id,
                                              @RequestBody @Valid final StationRequest stationRequest) {
        stationService.updateStation(id, stationRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);

        return ResponseEntity.noContent().build();
    }
}
