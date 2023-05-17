package subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;

import java.util.List;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public List<String> findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        return pathService.findShortestPath(source, target);
    }
}
