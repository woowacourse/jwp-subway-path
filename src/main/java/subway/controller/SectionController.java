package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.InitSectionRequest;
import subway.dto.SectionAtLastRequest;
import subway.dto.SectionRequest;
import subway.service.SectionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/lines/{lineId}")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/init-sections")
    public ResponseEntity<Void> createInitSections(@Valid @RequestBody InitSectionRequest request) {
        sectionService.saveInitSections(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/section")
    public ResponseEntity<Void> createSection(@Valid @RequestBody SectionRequest request) {
        sectionService.saveSection(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/last-section")
    public ResponseEntity<Void> createLastSection(@Valid @RequestBody SectionAtLastRequest request) {
        sectionService.saveSectionAtLast(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
