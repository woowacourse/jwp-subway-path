package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathFareService;
import subway.dto.PassengerDto;
import subway.dto.response.PathAndFareResponse;

@RestController
public class PathController {

    private final PathFareService pathFareService;

    public PathController(PathFareService pathFareService) {
        this.pathFareService = pathFareService;
    }

    @GetMapping("/paths/{startId}/{endId}")
    public ResponseEntity<PathAndFareResponse> findStationsInLine(@PathVariable Long startId, @PathVariable Long endId, @RequestParam int age) {
        PassengerDto passengerDto = new PassengerDto(age);

        PathAndFareResponse response = pathFareService.calculateRouteFare(startId, endId, passengerDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
