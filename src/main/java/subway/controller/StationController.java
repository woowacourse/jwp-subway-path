package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationDeleteRequest;
import subway.dto.StationResponse;
import subway.service.StationService;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationAddRequest stationRequest) {
        StationResponse station = stationService.addStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + station.getId()))
                .body(station);
    }

    @DeleteMapping("/stations/station")
    public ResponseEntity<StationResponse> deleteStation(@Valid @RequestBody StationDeleteRequest stationDeleteRequest) {
        StationResponse stationResponse = stationService.deleteStation(stationDeleteRequest);
        return ResponseEntity
                .ok()
                .body(stationResponse);
    }

}
