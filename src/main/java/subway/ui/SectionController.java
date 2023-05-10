package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;

@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> insertSection(@RequestBody SectionCreateRequest req) {
        sectionService.insertSection(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest req) {
        sectionService.deleteSection(req);
        return ResponseEntity.noContent().build();
    }
}
