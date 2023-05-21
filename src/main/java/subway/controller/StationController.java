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
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // TODO: 2023/05/17 헤더값 문제??
    @PostMapping
    public ResponseEntity<List<StationResponse>> addStation(@Valid @RequestBody StationAddRequest stationRequest) {
        List<StationResponse> stationResponses = stationService.addStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + stationResponses.get(0).getId()))
                .body(stationResponses);
    }

    @DeleteMapping("/station")
    public ResponseEntity<List<StationResponse>> deleteStation(@Valid @RequestBody StationDeleteRequest stationDeleteRequest) {
        List<StationResponse> stationResponses = stationService.deleteStation(stationDeleteRequest);
        return ResponseEntity
                .ok()
                .body(stationResponses);
    }

}
