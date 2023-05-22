package subway.ui;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.path.PathService;
import subway.application.price.PriceService;
import subway.domain.path.Path;
import subway.domain.price.Price;
import subway.dto.path.PathResponse;
import subway.dto.response.Response;

@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;
    private final PriceService priceService;

    public PathController(PathService pathService, PriceService priceService) {
        this.pathService = pathService;
        this.priceService = priceService;
    }

    @GetMapping
    public ResponseEntity<Response> findPath(@RequestParam String originStation,
                                             @RequestParam String destinationStation) {
        Path path = pathService.findPath(originStation, destinationStation);

        List<String> stations = path.getStations();
        int totalDistance = path.getTotalDistance();
        Price price = priceService.calculate(path);
        return Response.ok()
                .message(originStation + "에서 " + destinationStation + "까지의 경로가 조회되었습니다.")
                .result(new PathResponse(stations, totalDistance, price.getAmount()))
                .build();
    }
}
