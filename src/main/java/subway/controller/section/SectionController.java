package subway.controller.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.section.dto.SectionCreateControllerRequest;
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
    public ResponseEntity<Void> createSection(@RequestBody SectionCreateControllerRequest sectionCreateControllerRequest) {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                sectionCreateControllerRequest.getUpStationId(),
                sectionCreateControllerRequest.getDownStationId(),
                sectionCreateControllerRequest.getDistance(),
                sectionCreateControllerRequest.getLineId());
        sectionService.insert(sectionCreateRequest);
        return ResponseEntity.ok().build();
    }
}
