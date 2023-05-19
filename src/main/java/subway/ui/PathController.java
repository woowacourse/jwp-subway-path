package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.service.SubwayService;

@RestController
@RequestMapping("/path")
public class PathController {

    private final SubwayService subwayService;

    public PathController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping
    public ResponseEntity<Void> findPath(
            @RequestParam("startstation") final Long startStationId,
            @RequestParam("destinationstation") final Long destinationStationId
    ) {
        System.out.println("====== PathController.findPath ======");
        System.out.println("startStationId = " + startStationId);
        System.out.println("destinationStationId = " + destinationStationId);
        return ResponseEntity.ok().build();
    }

}
