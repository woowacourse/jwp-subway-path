package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.application.dto.PathDto;
import subway.application.dto.StationDto;
import subway.presentation.dto.response.PathResponse;
import subway.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/subway/path")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/{startStationId}/{endStationId}")
    public ResponseEntity<PathResponse> findShortestPath(@PathVariable Long startStationId,
                                                         @PathVariable Long endStationId) {
        PathDto pathDto = pathService.findShortestPath(startStationId, endStationId);

        List<StationResponse> stationResponses = toStationResponse(pathDto.getPath());
        Integer cost = pathDto.getCost();

        return ResponseEntity.ok(new PathResponse(stationResponses, cost));
    }

    private List<StationResponse> toStationResponse(List<StationDto> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
