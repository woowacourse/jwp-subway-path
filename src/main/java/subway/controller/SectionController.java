package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.SectionRequest;
import subway.service.SectionService;

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
    public ResponseEntity<Void> createSection(@Valid @RequestBody SectionRequest request) {
        sectionService.saveSectionInLine(request);
        return ResponseEntity.created(URI.create("/line-stations/" + request.getLineId())).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam("station-id") Long stationId) {
        SectionDeleteRequest request = new SectionDeleteRequest(lineId, stationId);
        sectionService.removeStationFromLine(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
