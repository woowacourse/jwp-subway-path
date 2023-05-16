package subway.controller;

import org.springframework.web.bind.annotation.*;
import subway.service.StationService;
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

    @DeleteMapping("/{lineId}/{station}")
    public void deleteStationInLine(@PathVariable long lineId, @PathVariable String station) {
        stationService.deleteTargetStationInLine(lineId, station);
    }
}
