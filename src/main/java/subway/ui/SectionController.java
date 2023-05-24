package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import subway.application.SectionService;
import subway.dto.SectionRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> insertSection(@PathVariable Long lineId, @Valid @RequestBody SectionRequest sectionRequest) {
        Long sectionId = sectionService.insertSection(lineId, sectionRequest);
        final URI location = UriComponentsBuilder.fromPath("/lines/{lineId}/sections/{sectionId}")
                .build(lineId, sectionId);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        sectionService.deleteStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
