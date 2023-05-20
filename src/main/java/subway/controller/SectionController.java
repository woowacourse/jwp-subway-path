package subway.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SectionService;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.SectionUpdateRequest;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationResponse;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody SectionUpdateRequest sectionUpdateRequest) {
        final SectionResponse response = sectionService.createSection(sectionUpdateRequest);
        final URI uri = URI.create("/sections" + response.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> findAllByLine(@PathVariable(name = "lineId") long lineId) {
        final List<StationResponse> stations = sectionService.findAllByLine(lineId);
        return ResponseEntity.ok(stations);
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<SectionResponse> deleteSection(@PathVariable(name = "lineId") long lineId, @RequestBody SectionDeleteRequest sectionDeleteRequest) {
        sectionService.deleteSection(lineId, sectionDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
