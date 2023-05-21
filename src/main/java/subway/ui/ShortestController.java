package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.application.ShortestService;
import subway.dto.ShortestResponse;

@Controller
@RequestMapping("/paths")
public final class ShortestController {

    private final ShortestService shortestService;

    public ShortestController(final ShortestService shortestService) {
        this.shortestService = shortestService;
    }

    @GetMapping("/start/{start-station-id}/end/{end-station-id}")
    public ResponseEntity<ShortestResponse> getShortest(@PathVariable("start-station-id") final Long startId,
                                                        @PathVariable("end-station-id") final Long endId,
                                                        @RequestParam final int age) {
        final ShortestResponse result = shortestService.findShortest(startId, endId, age);

        return ResponseEntity.ok(result);
    }
}
