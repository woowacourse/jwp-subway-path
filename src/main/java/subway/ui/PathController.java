package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.LineQueryService;
import subway.application.PathService;
import subway.application.PriceService;
import subway.application.StationService;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Price;
import subway.domain.Station;
import subway.ui.dto.request.GetPathPriceRequest;
import subway.ui.dto.request.GetPathRequest;
import subway.ui.dto.response.ReadPathResponse;
import subway.ui.dto.response.ReadPriceResponse;
import subway.ui.dto.response.ReadStationResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;
    private final PriceService priceService;
    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathController(final PathService pathService,
                          final PriceService priceService,
                          final StationService stationService,
                          final LineQueryService lineQueryService) {
        this.pathService = pathService;
        this.priceService = priceService;
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    @GetMapping
    public ResponseEntity<ReadPathResponse> findShortestPath(@RequestBody final GetPathRequest request) {
        final Station sourceStation = stationService.findStationById(request.getSourceStationId());
        final Station targetStation = stationService.findStationById(request.getTargetStationId());
        final List<Line> lines = lineQueryService.findAllLine();

        final List<Station> stations = pathService.getStationsByShortestPath(sourceStation, targetStation, lines);
        final ReadPathResponse response = ReadPathResponse.from(stations.stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/price")
    public ResponseEntity<ReadPriceResponse> findPrice(@RequestBody final GetPathPriceRequest request) {
        final Station sourceStation = stationService.findStationById(request.getSourceStationId());
        final Station targetStation = stationService.findStationById(request.getTargetStationId());
        final List<Line> lines = lineQueryService.findAllLine();

        final Distance distance = pathService.getDistanceByShortestPath(sourceStation, targetStation, lines);
        final Price price = priceService.getSubwayFare(distance);
        final ReadPriceResponse response = ReadPriceResponse.from(price);

        return ResponseEntity.ok(response);
    }
}
