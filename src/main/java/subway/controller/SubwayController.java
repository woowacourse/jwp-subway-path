package subway.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.PassengerRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.service.SubwayService;

@RequestMapping("/subways")
@RestController
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@Valid @RequestBody PassengerRequest request) {
        final ShortestPathResponse response = subwayService.findShortestPath(request);
        return ResponseEntity.ok(response);
    }
}
