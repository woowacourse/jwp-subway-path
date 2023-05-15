package subway.controller;

import org.springframework.web.bind.annotation.*;
import subway.service.StationService;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public void registerStation(@RequestBody StationRegisterRequest stationRegisterRequest) {
        stationService.registerStation(stationRegisterRequest);
    }

    @DeleteMapping("/{id}/{station}")
    public void deleteStation(@PathVariable long id, @PathVariable String station) {
        stationService.deleteStation(id, station);
    }
}
