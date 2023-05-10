package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SectionService;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.net.URI;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> insertSection(@RequestBody SectionRequest sectionRequest) {
        Long sectionId = sectionService.insertSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + sectionId)).build();
    }

    @DeleteMapping("/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId, @RequestBody SectionDeleteRequest sectionRequest) {
        sectionService.deleteStation(stationId, sectionRequest);
        return ResponseEntity.noContent().build();
    }
}
