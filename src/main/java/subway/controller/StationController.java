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
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@Valid @RequestBody StationCreateRequest stationCreateRequest) {
        Long id = stationService.save(stationCreateRequest);
        return ResponseEntity.created(URI.create("/stations/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findStation(@PathVariable final Long id) {
        StationResponse stationResponse = stationService.findById(id);
        return ResponseEntity.ok(stationResponse);
    }
}
