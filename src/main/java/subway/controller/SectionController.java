package subway.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.service.SectionService;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@RequestBody SectionCreateRequest sectionCreateRequest) {
        Long lineId = sectionService.save(sectionCreateRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.delete(sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
