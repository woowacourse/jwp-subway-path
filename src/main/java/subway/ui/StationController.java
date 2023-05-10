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
import subway.application.dto.CreationStationDto;
import subway.ui.dto.request.CreationStationRequest;
import subway.ui.dto.response.CreationStationResponse;
import subway.ui.dto.response.ReadStationResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<CreationStationResponse> createStation(@RequestBody final CreationStationRequest requestDto) {
        final CreationStationDto stationDto = stationService.saveStation(requestDto.getName());
        final CreationStationResponse response = CreationStationResponse.from(stationDto);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadStationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.findAllStationResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStationResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok().body(stationService.findStationResponseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
