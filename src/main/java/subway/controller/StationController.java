package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;
import subway.service.StationCreateService;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationCreateService stationCreateService;

    public StationController(StationCreateService stationCreateService) {
        this.stationCreateService = stationCreateService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationAddRequest stationRequest) {
        StationResponse station = stationCreateService.createStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

}
