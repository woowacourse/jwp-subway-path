package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.dto.AddStationRequest;
import subway.application.SubwayService;

import java.net.URI;

@RestController
@RequestMapping("/line/stations")
public class StationController {
    private final SubwayService subwayService;

    public StationController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody AddStationRequest addStationRequest) {
        long stationId = subwayService.addStation(addStationRequest);
        return ResponseEntity.created(URI.create("/line/stations/" + stationId)).build();
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
//    public ResponseEntity<Void> updateStation(@PathVariable Long id, @RequestBody StationRequest stationRequest) {
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
