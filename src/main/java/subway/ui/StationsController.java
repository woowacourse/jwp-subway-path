package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationsService;
import subway.dto.StationRequest;
import subway.dto.StationsSavingRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class StationsController {
    private final StationsService stationsService;

    public StationsController(StationsService stationsService) {
        this.stationsService = stationsService;
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> initializeStations(@PathVariable long lineId, @RequestBody StationsSavingRequest stationsSavingRequest) {
        long savedId = stationsService.insert(lineId, stationsSavingRequest.getPreviousStationName(),
                stationsSavingRequest.getNextStationName(), stationsSavingRequest.getDistance(), stationsSavingRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", lineId, savedId))).build();
    }

    @DeleteMapping("/{lineId}/stations")
    public ResponseEntity<Void> deleteStations(@PathVariable long lineId, @RequestBody StationRequest stationRequest) {
        stationsService.deleteStation(lineId, stationRequest.getName());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleSQLException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
