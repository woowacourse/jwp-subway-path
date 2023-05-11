package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.EndSectionRequest;
import subway.dto.InitSectionRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionLastDeleteRequest;
import subway.dto.SectionRequest;
import subway.dto.StationResponse;
import subway.service.SectionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lines/{lineId}")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/init-sections")
    public ResponseEntity<Void> createInitSections(@Valid @RequestBody InitSectionRequest request) {
        sectionService.saveInitSections(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/section")
    public ResponseEntity<Void> createSection(@Valid @RequestBody SectionRequest request) {
        sectionService.saveSection(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/end-section")
    public ResponseEntity<Void> createEndSection(@Valid @RequestBody EndSectionRequest request) {
        sectionService.saveEndSection(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/section")
    public ResponseEntity<Void> deleteSection(@PathVariable("lineId") Long lineId, @RequestParam("station-id") Long stationId) {
        SectionDeleteRequest request = new SectionDeleteRequest(stationId, lineId);
        sectionService.removeSectionsByStationAndLine(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/end-section")
    public ResponseEntity<Void> deleteEndSection(@PathVariable("lineId") Long lineId, @RequestParam("station-id") Long stationId) {
        SectionDeleteRequest request = new SectionDeleteRequest(stationId, lineId);
        sectionService.removeEndSectionByStationAndLine(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/last-sections")
    public ResponseEntity<Void> deleteSectionsAtLast(@PathVariable("lineId") Long lineId, @RequestParam("upward-id") Long upwardId, @RequestParam("downward-id") Long downwardId) {
        SectionLastDeleteRequest request = new SectionLastDeleteRequest(lineId, upwardId, downwardId);
        sectionService.removeLastSectionInLine(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
