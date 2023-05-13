package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.SectionService;
import subway.dto.request.LineStationRequest;
import subway.dto.response.SectionResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/lines/{line_id}/stations")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService lineStationService) {
        this.sectionService = lineStationService;
    }

    @PostMapping
    ResponseEntity<SectionResponse> createSection(@PathVariable("line_id") Long lineId, @RequestBody @Valid LineStationRequest lineStationRequest) {
        Long upBoundStationId = lineStationRequest.getUpBoundStationId();
        Long downBoundStationId = lineStationRequest.getDownBoundStationId();
        int distance = lineStationRequest.getDistance();
        SectionResponse lineStationResponse = sectionService.addStationToLine(upBoundStationId, downBoundStationId, distance, lineId);
        return ResponseEntity.status(HttpStatus.CREATED).body(lineStationResponse);
    }

    @DeleteMapping("/{station_id}")
    ResponseEntity<Void> deleteStation(@PathVariable("line_id") Long lineId, @PathVariable("station_id") Long stationId) {
        sectionService.removeStationFromLine(stationId, lineId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
