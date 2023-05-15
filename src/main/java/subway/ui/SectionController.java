//package subway.ui;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import subway.application.RouteService;
//import subway.dto.SectionRequest;
//
//@RestController
//public class SectionController {
//
//    private final RouteService routeService;
//
//    public SectionController(final RouteService routeService) {
//        this.routeService = routeService;
//    }
//
//    @PostMapping("/sections")
//    public ResponseEntity<Void> createStation(@RequestParam Long lineId, @RequestBody SectionRequest sectionRequest) {
//        routeService.addSection(lineId, sectionRequest);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/sections")
//    public ResponseEntity<Void> deleteStation(@RequestParam Long lineId, @RequestParam Long stationId) {
//        routeService.deleteStationById(lineId, stationId);
//        return ResponseEntity.noContent().build();
//    }
//}
