package subway.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.service.StationService;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

@RestController
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/stations")
    public void registerStation(@RequestBody StationRegisterRequest stationRegisterRequest) {
        stationService.registerStation(stationRegisterRequest);
    }

    @DeleteMapping("/stations")
    public void deleteStation(@RequestBody StationDeleteRequest stationDeleteRequest) {
        stationService.deleteStation(stationDeleteRequest);
    }
}
