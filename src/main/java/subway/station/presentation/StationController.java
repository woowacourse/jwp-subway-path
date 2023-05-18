package subway.station.presentation;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.station.dto.StationCreateDto;
import subway.station.service.StationService;

@RestController
@RequestMapping(path = "/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final StationCreateDto stationCreateDto) {
        final Long stationId = stationService.create(stationCreateDto);
        return ResponseEntity.created(URI.create("/stations/" + stationId)).build();
    }
}
