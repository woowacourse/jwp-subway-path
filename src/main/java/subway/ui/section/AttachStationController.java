package subway.ui.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.section.AttachStationService;
import subway.ui.dto.request.SectionCreateRequest;

@RestController
public class AttachStationController {
    private final AttachStationService attachStationService;

    public AttachStationController(final AttachStationService attachStationService) {
        this.attachStationService = attachStationService;
    }

    @PostMapping("/line/{line_id}/station")
    public void createSection(@PathVariable("line_id") Long line_id, @RequestBody SectionCreateRequest sectionCreateRequest) {
        attachStationService.createSection(line_id, sectionCreateRequest);
        ResponseEntity.ok().build();
    }
}
