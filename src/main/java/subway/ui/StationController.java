package subway.ui;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import javax.validation.Valid;
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
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {
    
    private final StationService stationService;
    
    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }
    
    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid final StationRequest stationRequest) {
        final StationResponse station = this.stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }
    
    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(this.stationService.findAllStationResponses());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable final Long id) {
        return ResponseEntity.ok().body(this.stationService.findStationResponseById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStation(@PathVariable final Long id,
            @RequestBody final StationRequest stationRequest) {
        this.stationService.updateStation(id, stationRequest);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        this.stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }
}
