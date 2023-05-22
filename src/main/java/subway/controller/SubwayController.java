package subway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.request.PassengerRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.service.SubwayService;

@Tag(name = "Subway", description = "지하철 API Document")
@RequestMapping("/subways")
@RestController
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @Operation(summary = "경로 정보 조회 API", description = "출발역에서 도착역까지의 경로 정보를 조회합니다.")
    @GetMapping("/shortest-path")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@Valid @RequestBody PassengerRequest request) {
        final ShortestPathResponse response = subwayService.findShortestPath(request);
        return ResponseEntity.ok(response);
    }
}
