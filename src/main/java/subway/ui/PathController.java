package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathSelectRequest;
import subway.dto.ShortestPathSelectResponse;
import subway.dto.StationSelectResponse;

@RestController
public class PathController {

    @GetMapping("/path")
    public ResponseEntity<ShortestPathSelectResponse> findPath(@ModelAttribute PathSelectRequest pathSelectRequest) {
        return ResponseEntity.ok()
                .body(new ShortestPathSelectResponse(
                        List.of(
                                new StationSelectResponse("역삼역"),
                                new StationSelectResponse("강남역"),
                                new StationSelectResponse("신논현역")
                        ),
                        17,
                        1_250
                ));
    }

}
