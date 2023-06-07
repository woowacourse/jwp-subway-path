package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationCreateReqeust;
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

    @PostMapping
    public ResponseEntity<Void> addStation(@Valid @RequestBody StationAddRequest stationRequest) {
        stationService.addStation(stationRequest);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/station")
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationCreateReqeust stationCreateReqeust) {
        StationResponse station = stationService.createStation(stationCreateReqeust);
        return ResponseEntity
                .created(URI.create("/stations/station" + station.getId()))
                .body(station);
    }


    @DeleteMapping("/station")
    public ResponseEntity<List<StationResponse>> deleteStation(@Valid @RequestBody StationDeleteRequest stationDeleteRequest) {
        List<StationResponse> stationResponses = stationService.deleteStation(stationDeleteRequest);
        return ResponseEntity
                .ok()
                .body(stationResponses);
    }

}
