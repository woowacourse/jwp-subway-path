package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.*;

import java.util.List;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> searchPath(@RequestBody PathRequest request) {
        PathResponse response = new PathResponse(
                18,
                1250,
                List.of(
                        new SectionResponse(
                                new StationResponse(1L, "잠실새내"),
                                new StationResponse(2L, "잠실"),
                                new LineResponse(1L, "2호선", "초록"),
                                10
                        ),
                        new SectionResponse(
                                new StationResponse(2L, "잠실"),
                                new StationResponse(3L, "석촌"),
                                new LineResponse(2L, "8호선", "파랑"),
                                8
                        )
                )
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
