package subway.ui;

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
import subway.domain.Station;
import subway.ui.dto.station.StationCreateRequest;
import subway.ui.dto.station.StationResponse;
import subway.ui.dto.station.StationUpdateRequest;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
        @RequestBody @Valid StationCreateRequest stationCreateRequest) {
        StationResponse stationResponse = StationResponse.from(
            stationService.saveStation(stationCreateRequest));
        return ResponseEntity.ok().body(stationResponse);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponse> stationResponses = mapToStationResponse(
            stationService.findAllStations());
        return ResponseEntity.ok().body(stationResponses);
    }

    private List<StationResponse> mapToStationResponse(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponse> updateStation(@PathVariable Long id,
        @RequestBody @Valid StationUpdateRequest stationUpdateRequest) {
        StationResponse stationResponse = StationResponse.from(
            stationService.updateStation(stationUpdateRequest, id));
        return ResponseEntity.ok().body(stationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
