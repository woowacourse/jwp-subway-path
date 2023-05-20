package subway.ui;

//import subway.dto.StationResponse;
//import subway.entity.StationEntity;
//import subway.service.StationService;
//
//import java.net.URI;
//import java.sql.SQLException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/stations")
//public class StationController {
//    private final StationService stationService;
//
//    public StationController(StationService stationService) {
//        this.stationService = stationService;
//    }
//
//    @PostMapping
//    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
//        StationResponse station = stationService.saveStation(stationRequest);
//        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
//    }
//
//    @GetMapping("/test")
//    public ResponseEntity<List<StationEntity>> showAllStations() {
//        return ResponseEntity.ok().body(stationService.findAllStationTest());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<StationResponse>> showStations() {
//        return ResponseEntity.ok().body(stationService.findAllStationResponses(1L, 1L));
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
////        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
////    }
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
//}
