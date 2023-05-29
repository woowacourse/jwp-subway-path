package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.SectionRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@PathVariable Long lineId, @RequestBody @Valid SectionRequest sectionRequest) {
        sectionService.saveSection(lineId, sectionRequest);
        return ResponseEntity.created(URI.create("/lines/" + lineId + "/stations/")).build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable String name) {
        sectionService.deleteStationByName(lineId, name);
        return ResponseEntity.noContent().build();
    }
}
