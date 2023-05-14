package subway.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.ui.dto.SectionRequest;
import subway.ui.dto.SectionResponse;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public ResponseEntity<Void> createSection(@RequestBody SectionRequest sectionRequest) {
        SectionResponse sectionResponse = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/stations/" + sectionResponse.getId())).build();
    }
}
