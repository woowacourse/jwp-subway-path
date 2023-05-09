package subway.controller;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.StationCreateRequest;
import subway.service.StationService;

@RequestMapping("/stations")
@RestController
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
}
