package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathAndFee;
import subway.dto.request.PathRequest;
import subway.dto.response.PathAndFeeResponse;

import javax.validation.Valid;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping
    public ResponseEntity<PathAndFeeResponse> findShortestPathAndFee(@RequestBody @Valid PathRequest pathRequest) {
        PathAndFee pathAndFee = pathService.findShortestPathAndFee(pathRequest.getSourceStationId(), pathRequest.getTargetStationId());
        PathAndFeeResponse response = PathAndFeeResponse.of(pathAndFee);
        return ResponseEntity.ok(response);
    }
}
