package subway.controller.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.section.dto.SectionInsertWebRequest;
import subway.service.section.SectionService;
import subway.service.section.dto.SectionCreateRequest;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@RequestBody SectionInsertWebRequest sectionInsertWebRequest) {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                sectionInsertWebRequest.getUpStationId(),
                sectionInsertWebRequest.getDownStationId(),
                sectionInsertWebRequest.getDistance(),
                sectionInsertWebRequest.getLineId());
        sectionService.insert(sectionCreateRequest);
        return ResponseEntity.ok().build();
    }
}
