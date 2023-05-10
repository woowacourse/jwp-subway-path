package subway.ui;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.DeleteSectionRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@RequestMapping("/sections")
@RestController
public class SectionController {
    
    private final SectionService sectionService;
    
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }
    
    @GetMapping("/{lineId}")
    public ResponseEntity<List<SectionResponse>> getSections(@PathVariable final long lineId) {
        final List<SectionResponse> sectionResponses = this.sectionService.getSections(lineId);
        return ResponseEntity.ok(sectionResponses);
    }
    
    @PostMapping
    public ResponseEntity<List<SectionResponse>> addSection(@RequestBody final SectionRequest sectionRequest) {
        this.sectionService.validate(sectionRequest);
        
        final java.util.List<SectionResponse> sectionResponses = this.sectionService.saveSection(sectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionResponses);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteSection(
            @RequestBody final DeleteSectionRequest deleteSectionRequest) {
        this.sectionService.validate(deleteSectionRequest);
        this.sectionService.deleteSection(deleteSectionRequest);
        return ResponseEntity.ok().build();
    }
}
