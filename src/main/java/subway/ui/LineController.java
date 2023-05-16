package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterInnerStationRequest;
import subway.dto.RegisterLastStationRequest;
import subway.dto.RegisterStationsRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<LineResponse> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.updateLine(lineId, lineRequest);
        return ResponseEntity.ok().body(line);
    }

    @PostMapping("/{name}/stations")
    public ResponseEntity<Void> registerStations(@PathVariable String name, @RequestBody RegisterStationsRequest registerStationsRequest) {
        lineService.registerInitStations(name, registerStationsRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{name}/last-station")
    public ResponseEntity<Void> registerLastStation(@PathVariable String name, @RequestBody RegisterLastStationRequest registerLastStationRequest) {
        lineService.registerLastStation(name, registerLastStationRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{name}/inner-station")
    public ResponseEntity<Void> registerInnerStation(@PathVariable String name,
                                                     @RequestBody RegisterInnerStationRequest registerInnerStationRequest) {
        lineService.registerInnerStation(name, registerInnerStationRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> findAll() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @DeleteMapping("/{lineName}/stations/{stationName}")
    public ResponseEntity<Void> deleteStationInLine(@PathVariable String lineName, @PathVariable String stationName) {
        lineService.deleteStation(lineName, stationName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLineById(@PathVariable Long lineId) {
        lineService.deleteLineById(lineId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
