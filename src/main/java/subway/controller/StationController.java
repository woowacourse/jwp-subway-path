package subway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stations")
public class StationController {
//    private final StationService stationService;
//
//    public StationController(StationService stationService) {
//        this.stationService = stationService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> createStation(@RequestBody StationRequest stationSaveRequest) {
//        stationService.saveStation(stationSaveRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<StationResponse>> showStations() {
//        return ResponseEntity.ok().body(stationService.findAllStation());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
//        return ResponseEntity.ok().body(stationService.findStationById(id));
//    }
//
//    @DeleteMapping()
//    public ResponseEntity<Void> deleteStation(@RequestBody StationDeleteRequest request) {
//        stationService.deleteStationById(request);
//        return ResponseEntity.noContent().build();
//    }

}
