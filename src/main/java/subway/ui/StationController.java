package subway.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationAddRequest;
import subway.dto.StationAddResponse;
import subway.dto.StationResponse;

@RequestMapping("/stations")
@RestController
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid StationAddRequest request) {
        StationAddResponse response = stationService.createStation(request);
        return ResponseEntity.created(URI.create("/stations/" + response.getId())).build();
    }

}
