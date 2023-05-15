package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionCreateRequest;
import subway.service.SectionService;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@Valid @RequestBody SectionCreateRequest sectionCreateRequest) {
        Long sectionId = sectionService.createSection(sectionCreateRequest);

        return ResponseEntity.created(URI.create("/sections/" + sectionId)).build();
    }
}
