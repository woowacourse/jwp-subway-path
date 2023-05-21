package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.path.PathService;
import subway.dto.path.PathResponse;
import subway.dto.response.Response;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<Response> findPath(@RequestParam String originStation,
                                             @RequestParam String destinationStation) {
        PathResponse pathResponse = pathService.findPath(originStation, destinationStation);
        return Response.ok()
                .message(originStation + "에서 " + destinationStation + "까지의 경로가 조회되었습니다.")
                .result(pathResponse)
                .build();
    }
}
