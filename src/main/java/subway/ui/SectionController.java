package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;

import java.util.List;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("lines/{lineId}/sections")
    public ResponseEntity<List<StationResponse>> addSection(@PathVariable long lineId) {
        sectionService.findStationsInOrder(lineId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("lines/{lineId}/sections")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @RequestBody SectionSaveRequest request) {
        sectionService.addSection(lineId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("lines/{lineId}/sections/{stationId}")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @PathVariable long stationId) {
        sectionService.removeStation(stationId, lineId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
