package subway.station.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.station.facade.StationFacade;
import subway.station.presentation.dto.response.StationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationFacade stationFacade;

    public StationController(final StationFacade stationFacade) {
        this.stationFacade = stationFacade;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(
            @RequestParam String name
    ) {
        Long stationId = stationFacade.createStation(name);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showAllStations() {
        return ResponseEntity.ok().body(stationFacade.getAll());
    }

    @PutMapping("/{stationId}")
    public ResponseEntity<Void> updateStation(
            @PathVariable Long stationId,
            @RequestParam String name
    ) {
        stationFacade.updateById(stationId, name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(
            @RequestParam Long lineId,
            @RequestParam Long stationId
    ) {
        stationFacade.deleteById(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
