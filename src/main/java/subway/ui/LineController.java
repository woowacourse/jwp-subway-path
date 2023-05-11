package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineResponse;

import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineResponse>> findAllLines() {
        final List<LineResponse> findAllLines = lineService.findAllLines();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findAllLines);
    }

    @GetMapping("/{lineName}/stations")
    public ResponseEntity<LineResponse> findLineStations(@PathVariable String lineName) {
        final LineResponse stations = lineService.findStationsByLineName(lineName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stations);
    }
}
