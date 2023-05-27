package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.entity.StationEntity;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationEntity> createStation(@RequestBody @Valid final StationRequest stationRequest) {
        Long id = stationService.save(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStation(@PathVariable final Long id) {
        stationService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<StationsResponse> findAll() {
        return ResponseEntity.ok().body(stationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok().body(stationService.findById(id));
    }
}
