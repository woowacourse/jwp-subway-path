package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.entity.StationEntity;
import subway.service.StationService;

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
    public ResponseEntity<StationsResponse> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
