package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationsService;
import subway.dto.StationsRequest;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class StationsController {
    private final StationsService stationsService;

    public StationsController(StationsService stationsService) {
        this.stationsService = stationsService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> initializeStations(@PathVariable long id, @RequestBody StationsRequest stationsRequest) {
        long savedId = stationsService.insert(id, stationsRequest.getPreviousStationName(),
                stationsRequest.getNextStationName(), stationsRequest.getDistance(), stationsRequest.isDown());
        return ResponseEntity.created(URI.create(String.format("/lines/%d/%d", id, savedId))).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleSQLException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
