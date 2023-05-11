package subway.station;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.station.domain.Station;
import subway.station.dto.StationCreateDto;

@RestController
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/station")
    public void create(@RequestBody StationCreateDto stationCreateDto) {
        stationService.create(Station.register(stationCreateDto.getName()));
    }
}
