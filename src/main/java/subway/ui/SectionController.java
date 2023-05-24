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

    @GetMapping("lines/{lineId}/stations")
    public ResponseEntity<List<StationResponse>> findStationsInLine(@PathVariable long lineId) {
        List<StationResponse> stationsInOrder = sectionService.findByLineId(lineId);
        return ResponseEntity.status(HttpStatus.OK).body(stationsInOrder);
    }

    @PostMapping("lines/{lineId}/stations")
    public ResponseEntity<Void> addSection(@PathVariable long lineId, @RequestBody SectionSaveRequest request) {
        sectionService.addSection(lineId, request);
        String location1 = "/stations/"+request.getUpStationId();
        String location2 = "/stations/"+request.getDownStationId();
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", location1, location2).build();
    }

    @DeleteMapping("lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeStationInLine(@PathVariable long lineId, @PathVariable long stationId) {
        sectionService.removeStation(stationId, lineId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
