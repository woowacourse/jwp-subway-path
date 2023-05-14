package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.ui.dto.StationCreateRequest;
import subway.ui.dto.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        final long stationId = stationService.createStation(stationCreateRequest).getId();
        final URI uri =  URI.create("/stations/" + stationId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findAllStations() {
        final List<StationResponse> stations = stationService.findAll();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findStationById(@PathVariable(name = "id") long stationId){
        final StationResponse stationResponse = stationService.findById(stationId);
        return ResponseEntity.ok(stationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
