package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.AddOneSectionRequest;
import subway.dto.AddTwoSectionRequest;
import java.net.URI;

@RestController
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}")
    public ResponseEntity<Void> addOneSection(@PathVariable Long lineId, @RequestBody AddOneSectionRequest addOneSectionRequest) {
        sectionService.addOneSection(lineId, addOneSectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addTwoSection(@PathVariable Long lineId, @RequestBody AddTwoSectionRequest addTwoSectionRequest) {
        sectionService.addTwoSections(lineId, addTwoSectionRequest);

        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }
}
