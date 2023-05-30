package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/subway")
public class SubwayController {
    private final SubwayService subwayService;

    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getPath(@RequestParam final long from, @RequestParam final long to) {
        return ResponseEntity.ok(subwayService.findPathBetween(from, to));
    }
}
