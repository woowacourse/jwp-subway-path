package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.AddStationRequest;

import java.net.URI;

@RestController
@RequestMapping("/line/stations")
public class StationController {

    private final SubwayService subwayService;

    public StationController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(@RequestBody AddStationRequest addStationRequest) {
        long stationId = subwayService.addStation(addStationRequest);
        return ResponseEntity.created(URI.create("/line/stations/" + stationId)).build();
    }
}
