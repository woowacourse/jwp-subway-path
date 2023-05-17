package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.domain.Station;
import subway.ui.dto.request.CreationStationRequest;
import subway.ui.dto.response.CreationStationResponse;
import subway.ui.dto.response.ReadStationResponse;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<CreationStationResponse> createStation(@RequestBody final CreationStationRequest request) {
        final Station station = stationService.saveStation(request.getName());
        final CreationStationResponse response = CreationStationResponse.from(station);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStationResponse> showStation(@PathVariable final Long id) {
        final Station station = stationService.findStationById(id);
        final ReadStationResponse response = ReadStationResponse.from(station);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
