package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.dto.RouteDto;

@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(final RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/routes")
    public ResponseEntity<RouteDto> selectSectionFee(@RequestParam String startStationName,
                                                     @RequestParam String endStationName) {
        RouteDto feeBySection = routeService.getFeeByStations(startStationName, endStationName);

        return ResponseEntity.ok().body(feeBySection);
    }

}
