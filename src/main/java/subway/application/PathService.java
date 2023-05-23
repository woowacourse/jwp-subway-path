package subway.application;

import org.springframework.stereotype.Service;
import subway.application.charge.ChargePolicy;
import subway.domain.Station;
import subway.domain.Subway;
import subway.domain.path.ShortestPath;
import subway.domain.path.SubwayGraph;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final ChargePolicy chargePolicy;

    public PathService(LineService lineService,
                       StationService stationService,
                       ChargePolicy chargePolicy) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.chargePolicy = chargePolicy;
    }

    public PathResponse findShortestPath(PathRequest request) {
        Station source = stationService.findByName(request.getSource());
        Station target = stationService.findByName(request.getTarget());
        validatePath(source, target);
        SubwayGraph graph = SubwayGraph.from(new Subway(lineService.findAllLines()));
        ShortestPath path = graph.findPath(source, target);
        int charge = chargePolicy.calculateFee(path.getDistance());
        return PathResponse.of(path, charge);
    }

    private void validatePath(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발지와 도착지는 같을 수 없습니다.");
        }
    }
}
