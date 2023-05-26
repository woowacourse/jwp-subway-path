package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.response.QueryShortestRouteResponse;
import subway.domain.Line;
import subway.domain.PassengerType;
import subway.domain.Station;
import subway.domain.fare.FareInfo;
import subway.domain.fare.discount.DiscountComposite;
import subway.domain.fare.expense.ExpenseComposite;
import subway.domain.route.Route;
import subway.domain.route.RouteFinder;
import subway.domain.vo.Money;
import subway.repository.FareCompositeRepository;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class RouteService {

    private final RouteFinder routeFinder;
    private final FareCompositeRepository fareCompositeRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public RouteService(
            final RouteFinder routeFinder,
            final FareCompositeRepository fareCompositeRepository,
            final LineRepository lineRepository,
            final StationRepository stationRepository
    ) {
        this.routeFinder = routeFinder;
        this.fareCompositeRepository = fareCompositeRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public QueryShortestRouteResponse findByStartAndEnd(
            final Integer passengerAge,
            final String startStationName,
            final String endStationName
    ) {
        final PassengerType passengerType = PassengerType.findBy(passengerAge);
        final Station startStation = getStationOrThrowException(startStationName, "이름으로 조회된 출발역이 존재하지 않습니다.");
        final Station endStation = getStationOrThrowException(endStationName, "이름으로 조회된 도착역이 존재하지 않습니다.");
        final List<Line> lines = lineRepository.findAll();

        final Route route = routeFinder.findRouteBy(lines, startStation, endStation);
        final Money totalFare = calculateFare(passengerType, route);

        return QueryShortestRouteResponse.from(route, totalFare);
    }

    private Station getStationOrThrowException(final String startStationName, final String message) {
        return stationRepository.findByStationName(startStationName)
                .orElseThrow(() -> new IllegalArgumentException(message));
    }

    private Money calculateFare(final PassengerType passengerType, final Route route) {
        final ExpenseComposite expenseComposite = fareCompositeRepository.collectExpenseComposite();
        final DiscountComposite discountComposite = fareCompositeRepository.collectDiscountComposite();

        FareInfo fareInfo = FareInfo.from(passengerType, route);
        fareInfo = expenseComposite.doImpose(fareInfo);
        fareInfo = discountComposite.doDiscount(fareInfo);

        return fareInfo.getTotalFare();
    }
}
