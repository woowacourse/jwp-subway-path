package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.service.StationService;
import subway.ui.dto.StationRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@Valid @RequestBody final StationRequest stationRequest) {
        Long stationId = stationService.create(stationRequest.getName());
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }
}
