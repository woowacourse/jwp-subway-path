package subway.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import subway.domain.price.Price;
import subway.domain.Station;
import subway.ui.dto.request.ReadPathPriceRequest;
import subway.ui.dto.response.ReadPathResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/path")
@Tag(name = "Path", description = "경로 API")
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
    @Operation(summary = "경로와 요금 확인 API", description = "출발점과 도착점 사이의 경로와 금액을 조회합니다.")
    public ResponseEntity<ReadPathResponse> findShortestPath(@Valid @RequestBody final ReadPathPriceRequest request) {
        final Station sourceStation = stationService.findStationById(request.getSourceStationId());
        final Station targetStation = stationService.findStationById(request.getTargetStationId());
        final List<Line> lines = lineQueryService.findAllLine();

        final List<Station> stations = pathService.getStationsByShortestPath(sourceStation, targetStation, lines);
        final Distance distance = pathService.getDistanceByShortestPath(sourceStation, targetStation, lines);
        final Price price = priceService.getSubwayFare(distance);

        final ReadPathResponse response = ReadPathResponse.from(stations, price);

        return ResponseEntity.ok(response);
    }
}
