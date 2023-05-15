package subway.ui.station;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.station.CreateStationService;
import subway.ui.dto.request.StationCreateRequest;
import subway.ui.dto.response.StationResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/stations")
public class CreateStationController {
    private final CreateStationService stationService;

    public CreateStationController(CreateStationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid StationCreateRequest stationCreateRequest) {
        final Long stationId = stationService.createStation(stationCreateRequest);

        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }
}
