package subway.ui.section;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> createSection(@PathVariable("line_id") Long line_id, @RequestBody SectionCreateRequest sectionCreateRequest) {
        attachStationService.createSection(line_id, sectionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
