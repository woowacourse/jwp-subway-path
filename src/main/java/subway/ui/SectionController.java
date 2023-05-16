package subway.ui;

import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

//    @PostMapping("/sections")
//    public ResponseEntity<Void> createStation(@RequestParam Long lineId, @RequestBody SectionRequest sectionRequest) {
//        sectionService.addSection(lineId, sectionRequest);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/sections")
//    public ResponseEntity<Void> deleteStation(@RequestParam Long lineId, @RequestParam Long stationId) {
//        sectionService.deleteStationById(lineId, stationId);
//        return ResponseEntity.noContent().build();
//    }
}
