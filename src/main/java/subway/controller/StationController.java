package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.StationCreateRequest;
import subway.controller.dto.StationResponse;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    private StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@Valid @RequestBody StationCreateRequest request) {
        final Long stationId = stationService.createStation(request);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findStationById(@PathVariable(name = "id") Long stationId) {
        final StationResponse response = stationService.findStationById(stationId);
        return ResponseEntity.ok(response);
    }
}
