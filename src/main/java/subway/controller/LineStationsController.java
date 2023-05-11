package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.StationResponse;
import subway.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/line-stations")
public class LineStationsController {

    private final SectionService sectionService;

    public LineStationsController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> readAllStationsOfLine(@PathVariable("lineId") Long lineId) {
        List<StationResponse> stationResponses = sectionService.readAllStationsOfLine(lineId);
        return ResponseEntity.ok().body(stationResponses);
    }
}
