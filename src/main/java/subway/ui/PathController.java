package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.api.PathResponse;
import subway.dto.api.ShortestPathResponse;
import subway.dto.domain.LineDto;
import subway.dto.domain.StationDto;

@RestController
@RequestMapping("/path")
public class PathController {

    @GetMapping
    public ResponseEntity<ShortestPathResponse> getPath(@RequestParam Long departureStationId,
                                                        @RequestParam Long arrivalStationId) {
        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(
                new StationDto(departureStationId, "잠실"),
                new StationDto(arrivalStationId, "역삼"),
                true,
                10,
                1_250,
                List.of(
                        new PathResponse(
                                new LineDto(1L, "2호선", "초록색"),
                                List.of(
                                        new StationDto(departureStationId, "잠실"),
                                        new StationDto(2L, "잠실새내"),
                                        new StationDto(4L, "선릉"),
                                        new StationDto(arrivalStationId, "역삼")
                                )
                        )
                )
        );

        return ResponseEntity.ok().body(shortestPathResponse);
    }
}
