package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.SubwayResponse;

@RequestMapping("/lines")
@RestController
public class SubwayController {
    
    private final SubwayService subwayService;
    
    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }
    
    @GetMapping("/{id}/stations")
    public ResponseEntity<SubwayResponse> getStations(@PathVariable final Long id) {
        final SubwayResponse allStationsInLine = this.subwayService.findAllStationsInLine(id);
        return ResponseEntity.ok().body(allStationsInLine);
    }
    
    @GetMapping("/stations")
    public ResponseEntity<List<SubwayResponse>> getStations() {
        final List<SubwayResponse> allStations = this.subwayService.findAllStations();
        return ResponseEntity.ok().body(allStations);
    }
}
