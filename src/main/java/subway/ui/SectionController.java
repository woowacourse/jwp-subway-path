package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import subway.application.SectionService;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> insertSection(@Valid @RequestBody SectionRequest sectionRequest) {
        Long sectionId = sectionService.insertSection(sectionRequest);
        final URI location = UriComponentsBuilder.fromPath("/sections/{sectionId}")
                .build(sectionId);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId, @Valid @RequestBody SectionDeleteRequest sectionRequest) {
        sectionService.deleteStation(stationId, sectionRequest);
        return ResponseEntity.noContent().build();
    }
}
