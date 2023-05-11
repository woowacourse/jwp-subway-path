package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionSaveRequest;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @RequestBody SectionSaveRequest request) {
        sectionService.addSection(lineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
