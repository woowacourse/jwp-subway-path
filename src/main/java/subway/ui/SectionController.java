package subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionResponse;

@RequestMapping("/sections")
@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/initial")
    public ResponseEntity<SectionResponse> addInitialSection(
        @RequestBody InitialSectionAddRequest initialSectionAddRequest) {
        SectionResponse sectionResponse = sectionService.addSection(initialSectionAddRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionResponse.getId()))
            .body(sectionResponse);
    }

    @PostMapping
    public ResponseEntity<List<SectionResponse>> addSection(@RequestBody SectionAddRequest sectionAddRequest) {
        List<SectionResponse> sectionResponses = sectionService.addSection(sectionAddRequest);
        return ResponseEntity.created(
                URI.create("/sections/" + sectionResponses.get(0).getId() + "," + sectionResponses.get(1).getId()))
            .body(sectionResponses);
    }
}
