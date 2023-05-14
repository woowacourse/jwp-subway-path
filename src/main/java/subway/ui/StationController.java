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
    public ResponseEntity<String> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        stationService.createStation(stationCreateRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findAllStations() {
        return ResponseEntity.ok().body(stationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
