package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.net.URI;

@RestController
@RequestMapping("stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/init")
    public ResponseEntity<StationInitResponse> registerInitStations(@RequestBody final StationInitRequest stationInitRequest) {
        StationInitResponse stationInitResponse = stationService.saveInitStations(stationInitRequest);
        return ResponseEntity.created(URI.create("station/init/" + stationInitResponse.getUpboundStationId()))
                .body(stationInitResponse);
    }

    @PostMapping
    public ResponseEntity<StationResponse> registerStation(@RequestBody final StationRequest stationRequest) {
        StationResponse stationResponse = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("station/" + stationResponse.getId()))
                .body(stationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok().build();
    }
}
