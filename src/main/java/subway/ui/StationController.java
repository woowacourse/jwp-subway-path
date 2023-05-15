package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.StationService;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;

import java.net.URI;

@RestController
@RequestMapping("stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/init")
    public ResponseEntity<StationInitResponse> registerInitStations(final StationInitRequest stationInitRequest) {
        StationInitResponse stationInitResponse = stationService.saveInitStations(stationInitRequest);
        return ResponseEntity.created(URI.create("station/init/" + stationInitResponse.getUpboundStationId()))
                .body(stationInitResponse);
    }
}
