package subway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines")
public class LineController {
//
//    private final LineService lineService;
//    private final StationService stationService;
////    private final SectionService sectionService;
//
//    public LineController(final LineService lineService, final StationService stationService,
//                          final SectionService sectionService) {
//        this.lineService = lineService;
//        this.stationService = stationService;
//        this.sectionService = sectionService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> createLine(@RequestBody LineRequest lineRequest) {
//        final Long id = lineService.saveLine(lineRequest);
//        return ResponseEntity.created(URI.create("/lines/" + id)).build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<LineResponse>> findAllLines() {
//        return ResponseEntity.ok(lineService.findLineResponses());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
//        return ResponseEntity.ok(lineService.findLineResponseById(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
//        lineService.updateLine(id, lineUpdateRequest);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
//        lineService.deleteLineById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}/sections")
//    public ResponseEntity<List<StationResponse>> findAllStationOrderBySection() {
//        return ResponseEntity.ok(stationService.findAllStationOrderBySection());
//    }
//

}
