package subway.controller.section;

import org.springframework.stereotype.Controller;
import subway.service.section.SectionService;

@Controller
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

//    @PostMapping("/sections")
//    public ResponseEntity<Void> createSection(@RequestBody SectionCreateRequest sectionCreateRequest, @PathVariable Long lineId) {
//        subway.service.section.dto.SectionCreateRequest sectionCreateRequest1 = new subway.service.section.dto.SectionCreateRequest(
//                sectionCreateRequest.getUpStationName(),
//                sectionCreateRequest.getDownStationName(),
//                sectionCreateRequest.getDistance()
//        );
//        sectionService.insert(sectionCreateRequest1, lineId);
//        return ResponseEntity.ok().build();
//    }
}
