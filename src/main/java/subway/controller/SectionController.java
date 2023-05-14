package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.SectionCreationRequest;
import subway.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@Valid @RequestBody SectionCreationRequest request) {
        sectionService.saveSection(request);
        return ResponseEntity.created(URI.create("/lines/" + request.getLineId())).build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteSections(@PathVariable long lineId, @PathVariable long stationId) {
        sectionService.deleteSections(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
