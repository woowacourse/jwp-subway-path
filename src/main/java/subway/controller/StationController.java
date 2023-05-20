package subway.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.StationCreateRequest;
import subway.dto.StationDeleteRequest;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody StationCreateRequest stationCreateRequest) {
        Long id = stationService.save(stationCreateRequest);
        return ResponseEntity.created(URI.create("/stations/" + id)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(@RequestBody StationDeleteRequest stationDeleteRequest) {
        stationService.delete(stationDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
