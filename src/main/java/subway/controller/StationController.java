package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.entity.StationEntity;
import subway.service.StationService;

import javax.validation.Valid;
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
    public ResponseEntity<StationEntity> createStation(@RequestBody @Valid final StationRequest stationRequest) {
        Long id = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findStationEntityById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
