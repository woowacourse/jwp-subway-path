package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.RouteService;
import subway.application.StationNamesConvertor;
import subway.domain.Route;
import subway.dto.DistanceDto;
import subway.dto.FeeDto;
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
        Route route = routeService.getFeeByStations(startStationName, endStationName);

        RouteDto routeDto = new RouteDto(
                new DistanceDto(route.getDistance().getDistance()),
                new FeeDto(route.getFee().getFee()),
                StationNamesConvertor.convertToStationNamesByStations(route.getStations())
        );

        return ResponseEntity.ok().body(routeDto);
    }

}
