package subway.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionCreateService;
import subway.application.SectionDeleteService;
import subway.controller.dto.SectionDeleteRequest;
import subway.controller.dto.SectionRequest;

@RestController
public class SectionController {

    private final SectionCreateService sectionCreateService;
    private final SectionDeleteService sectionDeleteService;

    public SectionController(SectionCreateService sectionCreateService, SectionDeleteService sectionDeleteService) {
        this.sectionCreateService = sectionCreateService;
        this.sectionDeleteService = sectionDeleteService;
    }

    @PostMapping("/sections")
    public ResponseEntity<Void> createSection(@RequestBody SectionRequest sectionRequest) {
        sectionCreateService.createSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections")).build();
    }

    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest deleteRequest) {
        sectionDeleteService.deleteSection(deleteRequest);
        return ResponseEntity.noContent().build();
    }
}
