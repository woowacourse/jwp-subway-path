package subway.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.service.SectionService;

@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> addSection(@RequestBody @Valid final SectionCreateRequest sectionCreateRequest) {
        sectionService.addSection(sectionCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSection(@RequestBody @Valid final SectionDeleteRequest sectionDeleteRequest) {
        sectionService.removeSection(sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
