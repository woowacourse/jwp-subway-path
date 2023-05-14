package subway.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationCreateResponse;
import subway.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationCreateResponse> createStation(@RequestBody StationCreateRequest createRequest) {
        Station createdStation = stationService.saveStation(createRequest);
        StationCreateResponse stationCreateResponse = new StationCreateResponse(createdStation.getId(), createdStation.getName());
        return ResponseEntity
                .created(URI.create("/stations/" + stationCreateResponse.getStationId()))
                .body(stationCreateResponse);
    }
}
