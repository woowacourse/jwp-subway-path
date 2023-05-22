package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.service.StationService;
import subway.service.dto.request.StationRegisterRequest;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity registerStation(@RequestBody StationRegisterRequest stationRegisterRequest) {
        stationService.registerStation(stationRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/{stationName}")
    public ResponseEntity deleteStationInLine(@PathVariable long lineId, @PathVariable String stationName) {
        stationService.deleteTargetStationInLine(lineId, stationName);
        return ResponseEntity.noContent().build();

    }
}
