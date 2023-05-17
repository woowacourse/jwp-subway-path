package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.SectionCreationRequest;
import subway.dto.response.LineResponse;
import subway.service.SectionService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createSection(@PathVariable final long lineId, @Valid @RequestBody final SectionCreationRequest request) {
        LineResponse lineResponse = sectionService.saveSection(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).body(lineResponse);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteSections(@PathVariable final long lineId, @PathVariable final long stationId) {
        sectionService.deleteSections(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
