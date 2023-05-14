package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.service.FeeService;
import subway.dto.response.ShortestWayResponse;

@RequestMapping("/fee")
@RestController
public class FeeController {

    private final FeeService feeService;

    public FeeController(final FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping
    public ResponseEntity<ShortestWayResponse> findShortestWay(
            @RequestParam("start") Long startStationId,
            @RequestParam("end") Long endStationId
    ) {
        final var shortestWayResponse = feeService.showShortestWay(startStationId, endStationId);

        return ResponseEntity.ok(shortestWayResponse);
    }
}
