package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.LineService;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import subway.dto.RegisterInnerStationRequest;
import subway.dto.RegisterLastStationRequest;
import subway.dto.RegisterStationsRequest;

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
    public ResponseEntity<Void> registerInnerStation(@PathVariable String name, @RequestBody RegisterInnerStationRequest registerInnerStationRequest) {
        lineService.registerInnerStation(name, registerInnerStationRequest);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
//        return ResponseEntity.ok(lineService.findLineResponseById(id));
//    }

    @GetMapping("/{name}")
    public ResponseEntity<LineResponse> findLineByName(@PathVariable String name) {
        return ResponseEntity.ok(lineService.findLineResponseByName(name));
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> findAll() {
        return ResponseEntity.ok(lineService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineName}/stations/{stationName}")
    public ResponseEntity<Void> deleteStationInLine(@PathVariable String lineName, @PathVariable String stationName) {
        lineService.deleteStation(lineName, stationName);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
