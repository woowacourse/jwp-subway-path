package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> registerStation(
            @RequestParam String name
    ) {
        Long stationId = stationService.createStation(name);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> showStations(@PathVariable Long lineId) {
        return ResponseEntity.ok().body(stationService.getAllStationResponses(lineId));
    }

    @DeleteMapping("/{lineId}/{stationId}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable Long lineId,
            @PathVariable Long stationId
    ) {
        stationService.deleteStationById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
