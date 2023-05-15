package subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;
import subway.ui.dto.SectionResponse;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public ResponseEntity<Void> createSection(@RequestBody PostSectionRequest postSectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(postSectionRequest);
        return ResponseEntity.created(URI.create("/stations/" + sectionResponse.getId())).build();
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(@RequestBody DeleteSectionRequest deleteSectionRequest) {
        sectionService.deleteSection(deleteSectionRequest);
        return ResponseEntity.noContent().build();
    }
}
