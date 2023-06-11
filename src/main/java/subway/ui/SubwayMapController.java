package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayMapService;
import subway.dto.SubwayMapResponse;

import java.util.List;

@RequestMapping("/lines")
@RestController
public class SubwayMapController {
    
    private final SubwayMapService subwayMapService;
    
    public SubwayMapController(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }
    
    @GetMapping("/{id}/stations")
    public ResponseEntity<SubwayMapResponse> getStations(@PathVariable final Long id) {
        final SubwayMapResponse allStationsInLine = this.subwayMapService.findAllStationsInLine(id);
        return ResponseEntity.ok().body(allStationsInLine);
    }
    
    @GetMapping("/stations")
    public ResponseEntity<List<SubwayMapResponse>> getStations() {
        final List<SubwayMapResponse> allStations = this.subwayMapService.findAllStations();
        return ResponseEntity.ok().body(allStations);
    }
}
