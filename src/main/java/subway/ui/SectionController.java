package subway.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.DeleteSectionRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@RestController
public class SectionController {
    
    private final SectionService sectionService;
    
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }
    
    @GetMapping("/lines/{lineId}/sections")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable final long lineId) {
        final List<SectionResponse> sectionResponses = this.sectionService.findSectionsByLineId(lineId);
        return ResponseEntity.ok(sectionResponses);
    }
    
    @PostMapping("/sections")
    public ResponseEntity<List<SectionResponse>> addSection(@RequestBody @Valid final SectionRequest sectionRequest) {
        final List<SectionResponse> sectionResponses = this.sectionService.insertSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionRequest.getLineId())).body(sectionResponses);
    }
    
    @DeleteMapping("/sections")
    public ResponseEntity<Void> deleteSection(
            @RequestBody @Valid final DeleteSectionRequest deleteSectionRequest) {
        this.sectionService.deleteSection(deleteSectionRequest);
        return ResponseEntity.noContent().build();
    }
}
