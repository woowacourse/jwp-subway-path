package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@RestController
public class SectionController {
    
    private final SectionService sectionService;
    
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }
    
    @PostMapping("/section")
    public ResponseEntity<SectionResponse> addSection(@RequestBody final SectionRequest sectionRequest) {
        this.sectionService.validate(sectionRequest);
        
        final SectionResponse sectionResponse = this.sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId())).body(sectionResponse);
    }
}
