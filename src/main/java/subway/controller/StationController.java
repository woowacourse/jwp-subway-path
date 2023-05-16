package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationDeleteRequest;
import subway.dto.StationResponse;
import subway.service.StationService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> addStation(@Valid @RequestBody StationAddRequest stationRequest) {
        StationResponse station = stationService.addStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + station.getId()))
                .body(station);
    }

    @DeleteMapping("/stations/station")
    public ResponseEntity<List<StationResponse>> deleteStation(@Valid @RequestBody StationDeleteRequest stationDeleteRequest) {
        List<StationResponse> stationResponses = stationService.deleteStation(stationDeleteRequest);
        return ResponseEntity
                .ok()
                .body(stationResponses);
    }

}
