package subway.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid StationRequest request) {
        StationResponse station = StationResponse.of(stationService.createStation(request));
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findAllStations() {
        List<StationResponse> stations = stationService.findStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationResponse> findStationById(@PathVariable Long stationId) {
        StationResponse station = StationResponse.of(stationService.findStationById(stationId));
        return ResponseEntity.ok(station);
    }

    @PutMapping("/{stationId}")
    public ResponseEntity<Void> updateStation(@PathVariable Long stationId, @RequestBody @Valid StationRequest request) {
        stationService.updateStation(stationId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> removeStation(@PathVariable Long stationId) {
        stationService.removeStation(stationId);
        return ResponseEntity.noContent().build();
    }

}
