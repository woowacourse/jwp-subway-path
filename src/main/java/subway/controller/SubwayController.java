package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.subway.Path;
import subway.dto.request.FindShortestRouteRequest;
import subway.dto.response.FindShortestRouteResponse;
import subway.service.SubwayService;

@RestController
@RequestMapping("/routes")
public class SubwayController {
    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping
    public ResponseEntity<FindShortestRouteResponse> findShortestRoute(
            @Valid @RequestBody FindShortestRouteRequest request) {
        Path path = subwayService.findShortestRoute(request.getPassengerAge(), request.getStartStationId(),
                request.getEndStationId());

        FindShortestRouteResponse response = FindShortestRouteResponse.from(path);
        return ResponseEntity.ok(response);
    }
}
