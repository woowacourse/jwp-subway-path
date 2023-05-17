package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.path.PathRequest;
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
    public ResponseEntity<Response> findPath(@RequestBody PathRequest pathRequest) {
        PathResponse pathResponse = pathService.findPath(pathRequest);
        return Response.ok()
                .message(pathRequest.getOriginStationName() + "에서 " + pathRequest.getDestinationStationName()
                        + "까지의 경로가 조회되었습니다.")
                .result(pathResponse)
                .build();
    }
}
