package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.ui.dto.SectionDeleteRequest;
import subway.ui.dto.SectionRequest;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public ResponseEntity<Void> createSection(@RequestBody SectionRequest sectionRequest) {
        sectionService.createSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections")).build();
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest deleteRequest) {
        sectionService.deleteSection(deleteRequest);
        return ResponseEntity.noContent().build();
    }
}
