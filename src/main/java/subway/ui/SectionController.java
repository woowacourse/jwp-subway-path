package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.ui.dto.request.SectionCreateRequest;
import subway.ui.dto.request.SectionDeleteRequest;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@RequestBody SectionCreateRequest sectionCreateRequest) {
        sectionService.createSection(sectionCreateRequest);
        return ResponseEntity.created(URI.create("/sections")).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest deleteRequest) {
        sectionService.deleteSection(deleteRequest);
        return ResponseEntity.noContent().build();
    }
}
