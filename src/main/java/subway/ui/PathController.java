package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineQueryService;
import subway.application.PathService;
import subway.application.StationService;
import subway.domain.Line;
import subway.domain.Station;
import subway.ui.dto.request.GetPathRequest;
import subway.ui.dto.response.CreationPathResponse;
import subway.ui.dto.response.ReadStationResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;
    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathController(final PathService pathService, final StationService stationService, final LineQueryService lineQueryService) {
        this.pathService = pathService;
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    @GetMapping
    public ResponseEntity<CreationPathResponse> findShortestPath(@RequestBody final GetPathRequest request) {
        final Station startStation = stationService.findStationById(request.getSourceStationId());
        final Station endStation = stationService.findStationById(request.getTargetStationId());
        final List<Line> lines = lineQueryService.findAllLine();

        final List<Station> stations = pathService.getStationsByShortestPath(startStation, endStation, lines);
        final CreationPathResponse response = CreationPathResponse.from(stations.stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}
