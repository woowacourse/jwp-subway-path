package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.api.StationRequest;
import subway.dto.api.StationResponse;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        Long stationId = stationService.create(stationRequest.getName());
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }
}
