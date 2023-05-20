//package subway.ui;
//
//import java.net.URI;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import subway.application.SectionService;
//import subway.ui.dto.SectionRequest;
//import subway.ui.dto.StationResponse;
//
//@RestController
//@RequestMapping("/sections")
//public class SectionController {
//
//    private final SectionService sectionService;
//
//    public SectionController(SectionService sectionService) {
//        this.sectionService = sectionService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> createStation(@RequestBody SectionRequest stationRequest) {
//        sectionService.addStation(stationRequest);
//        return ResponseEntity.created(URI.create("/stations/")).build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<StationResponse>> findStations() {
//        List<StationResponse> stationResponses = sectionService.findAllStations().stream()
//                .map(StationResponse::of)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(stationResponses);
//    }
//
//    @GetMapping("/{stationId}")
//    public ResponseEntity<StationResponse> findStationOfLine(@PathVariable Long stationId, @RequestParam Long lineId) {
//        StationResponse station = StationResponse.of(sectionService.findStationBy(stationId, lineId));
//        return ResponseEntity.ok().body(station);
//    }
//
////    @PutMapping("/{stationId}")
////    public ResponseEntity<Void> updateStationOfLine(@PathVariable Long stationId, @RequestParam Long lineId) {
////        stationService.updateStationBy(stationId, lineId);
////        return ResponseEntity.noContent().build();
////    }
//
//    @DeleteMapping("/{stationId}")
//    public ResponseEntity<Void> removeStationOfLine(@PathVariable Long stationId, @RequestParam Long lineId) {
//        sectionService.deleteStationBy(stationId, lineId);
//        return ResponseEntity.noContent().build();
//    }
//
//}
