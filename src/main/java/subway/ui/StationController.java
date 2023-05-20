package subway.ui;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationCreateRequest;
import subway.dto.StationInitialCreateRequest;
import subway.dto.StationResponse;

@RestController
@RequestMapping("/lines")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<List<StationResponse>> createInitialStations (@PathVariable Long lineId, @RequestBody @Valid StationInitialCreateRequest stationInitialCreateRequest) {
        List<StationResponse> stations = stationService.createInitialStations(lineId, stationInitialCreateRequest).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(stations);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<StationResponse> createAdditionalStation(@PathVariable Long lineId, @RequestBody @Valid StationCreateRequest stationCreateRequest) {
        StationResponse station = StationResponse.of(stationService.createStation(lineId, stationCreateRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(station);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        stationService.removeStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

}
