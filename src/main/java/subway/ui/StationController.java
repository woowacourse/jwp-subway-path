package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;
import subway.application.StationService;
import subway.service.StationCreateService;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;
    private final StationCreateService stationCreateService;

    public StationController(StationService stationService, final StationCreateService stationCreateService) {
        this.stationService = stationService;
        this.stationCreateService = stationCreateService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationAddRequest stationRequest) {
        StationResponse station = stationCreateService.createStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

//    @GetMapping
//    public ResponseEntity<List<StationResponse>> showStations() {
//        return ResponseEntity.ok().body(stationService.findAllStationResponses());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
//        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationCreateRequest stationRequest) {
//        stationService.updateStation(id, stationRequest);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
//        stationService.deleteStationById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<Void> handleSQLException() {
//        return ResponseEntity.badRequest().build();
//    }
}
