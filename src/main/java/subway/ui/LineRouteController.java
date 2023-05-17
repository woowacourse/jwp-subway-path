package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineRouteService;
import subway.dto.LineRouteResponse;

@RequestMapping("/lines")
@RestController
public class LineRouteController {
    
    private final LineRouteService lineRouteService;
    
    public LineRouteController(final LineRouteService lineRouteService) {
        this.lineRouteService = lineRouteService;
    }
    
    @GetMapping("/{lineId}/stations")
    public ResponseEntity<LineRouteResponse> getStations(@PathVariable final Long lineId) {
        final LineRouteResponse allStationsInLine = this.lineRouteService.findAllStationsInLine(lineId);
        return ResponseEntity.ok().body(allStationsInLine);
    }
    
    @GetMapping("/stations")
    public ResponseEntity<List<LineRouteResponse>> getStations() {
        final List<LineRouteResponse> allStations = this.lineRouteService.findAllStations();
        return ResponseEntity.ok().body(allStations);
    }
}
