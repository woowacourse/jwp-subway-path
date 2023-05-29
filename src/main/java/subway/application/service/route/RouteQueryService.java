package subway.application.service.route;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.route.FindRouteUseCase;
import subway.application.port.in.route.dto.command.FindRouteCommand;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.route.RouteFinderPort;
import subway.application.port.out.station.LoadStationPort;
import subway.application.service.exception.NoSuchStationException;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Line;
import subway.domain.route.Route;
import subway.domain.station.Station;

@Service
@Transactional(readOnly = true)
public class RouteQueryService implements FindRouteUseCase {

    private final LoadLinePort loadLinePort;
    private final LoadStationPort loadStationPort;
    private final RouteFinderPort routeFinderPort;
    private final FarePolicy farePolicy;

    public RouteQueryService(final LoadLinePort loadLinePort,
            final LoadStationPort loadStationPort,
            final RouteFinderPort routeFinderPort, final FarePolicy farePolicy) {
        this.loadLinePort = loadLinePort;
        this.loadStationPort = loadStationPort;
        this.routeFinderPort = routeFinderPort;
        this.farePolicy = farePolicy;
    }

    // TODO(질문): 요금 정보는 FindRouteUseCase에 포함되는건가..? 따로 CalculateFareUseCase로 분리하는게 올바른 방법일까?
    @Override
    public RouteQueryResponse findRoute(final FindRouteCommand command) {
        List<Line> lines = loadLinePort.findAll();
        Optional<Station> source = loadStationPort.findById(command.getSourceStationId());
        Optional<Station> target = loadStationPort.findById(command.getTargetStationId());
        if (source.isEmpty() || target.isEmpty()) {
            throw new NoSuchStationException();
        }

        Route route = routeFinderPort.findRoute(source.get(), target.get(), lines);

        Fare fare = farePolicy.calculate(route, command.getAge(), new Fare());

        return RouteMapper.toResponse(route, fare);
    }
}
