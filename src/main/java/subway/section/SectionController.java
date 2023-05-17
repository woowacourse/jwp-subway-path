//package subway.section;
//
//import java.net.URI;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import subway.section.dto.SectionCreateDto;
//
//@RestController
//@RequestMapping("/sections")
//public class SectionController {
//    private final SectionService sectionService;
//
//    public SectionController(final SectionService sectionService) {
//        this.sectionService = sectionService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> create(@RequestBody final SectionCreateDto sectionCreateDto) {
//        sectionService.addSection(sectionCreateDto);
//        return ResponseEntity.created(URI.create("/sections/" + )).build();
//    }
//
//    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
//    public ResponseEntity<Void> delete(@PathVariable(name = "lineId") final Long lineId,
//        @PathVariable(name = "stationId") final Long stationId) {
//        sectionService.removeStationBy(lineId, stationId);
//        return ResponseEntity.noContent().build();
//    }
//}
