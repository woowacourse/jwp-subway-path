package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.AddStationRequest;
import subway.dto.StationResponse;

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

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> getStation(@PathVariable long id) {
        return ResponseEntity.ok(subwayService.findStationById(id));
    }
}
