package subway.ui.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.section.FindStationService;
import subway.ui.dto.response.StationResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class FindLineController {

    private final FindStationService findStation;

    public FindLineController(final FindStationService findStation) {
        this.findStation = findStation;
    }

    @GetMapping("/{line_id}")
    public ResponseEntity<List<StationResponse>> findStationsByLine(@PathVariable("line_id") Long lineId) {
        return ResponseEntity.ok(findStation.findAllByLine(lineId));
    }

    @GetMapping
    public ResponseEntity<List<List<StationResponse>>> findALlStations() {
        return ResponseEntity.ok(findStation.findAllLine());
    }
}
