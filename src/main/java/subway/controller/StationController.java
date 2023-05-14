package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;
import subway.service.StationAddService;

import java.net.URI;

@RestController
public class StationController {
    private final StationAddService stationAddService;

    public StationController(StationAddService stationAddService) {
        this.stationAddService = stationAddService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationAddRequest stationRequest) {
        StationResponse station = stationAddService.addStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + station.getId()))
                .body(station);
    }

}
