package subway.application;

import static subway.application.mapper.SectionMapper.createSubwayLines;
import static subway.application.mapper.SectionMapper.getAllSections;
import static subway.application.mapper.SectionMapper.getSectionsByLineId;
import static subway.application.mapper.StationMapper.createStationResponses;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.domain.fare.ExtraFare;
import subway.domain.fare.Fare;
import subway.domain.fare.discount.ChildFarePolicy;
import subway.domain.fare.discount.DiscountFarePolicy;
import subway.domain.fare.discount.TeenagerFarePolicy;
import subway.domain.fare.normal.BasicFarePolicy;
import subway.domain.fare.normal.FarePolicy;
import subway.domain.fare.normal.UnitEightFarePolicy;
import subway.domain.fare.normal.UnitFiveFarePolicy;
import subway.domain.line.LineRepository;
import subway.domain.line.LineWithSectionRes;
import subway.domain.route.Distance;
import subway.domain.route.GraphProvider;
import subway.domain.route.ShortestRoute;
import subway.domain.section.Section;
import subway.domain.section.SubwayLine;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final GraphProvider graphProvider;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final List<FarePolicy> farePolicies;
    private final List<DiscountFarePolicy> discountFarePolicies;

    public RouteService(final GraphProvider graphProvider,
                        final LineRepository lineRepository,
                        final StationRepository stationRepository) {
        this.graphProvider = graphProvider;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicies = List.of(new BasicFarePolicy(), new UnitFiveFarePolicy(), new UnitEightFarePolicy());
        discountFarePolicies = List.of(new TeenagerFarePolicy(), new ChildFarePolicy());
    }

    public RouteResponse getShortestRouteAndFare(final Long sourceStationId, final Long targetStationId) {
        final Station sourceStation = stationRepository.findById(sourceStationId);
        final Station targetStation = stationRepository.findById(targetStationId);
        final List<LineWithSectionRes> possibleSections = lineRepository.getPossibleSections(
            sourceStationId, targetStationId);

        final Map<Long, List<LineWithSectionRes>> sectionsByLineId = getSectionsByLineId(possibleSections);
        final ShortestRoute shortestRoute = getShortestRoute(sourceStation, targetStation, sectionsByLineId);
        final List<Section> sections = getAllSections(sectionsByLineId);
        final Distance shortestDistance = shortestRoute.getShortestDistance(sections);

        final ExtraFare lineExtraFare = new ExtraFare(possibleSections);
        final Fare totalFare = getTotalFare(shortestDistance, lineExtraFare);
        final List<Fare> discountFare = getDiscountFares(totalFare);

        return new RouteResponse(
            createStationResponses(possibleSections, shortestRoute.getShortestRoutes()),
            getFareResponse(totalFare, discountFare));
    }

    private ShortestRoute getShortestRoute(final Station sourceStation, final Station targetStation,
                                           final Map<Long, List<LineWithSectionRes>> sectionsByLineId) {
        final List<SubwayLine> subwayLines = createSubwayLines(sectionsByLineId);
        final List<Station> shortestRoute = graphProvider.getShortestPath(subwayLines, sourceStation, targetStation);
        return new ShortestRoute(shortestRoute);
    }

    private Fare getTotalFare(final Distance shortestDistance, final ExtraFare extraFare) {
        return farePolicies.stream()
            .filter(policy -> policy.isAvailable(shortestDistance))
            .map(policy -> policy.calculateFare(shortestDistance))
            .findFirst()
            .orElseThrow()
            .add(extraFare.fare());
    }

    private List<Fare> getDiscountFares(final Fare totalFare) {
        return discountFarePolicies.stream()
            .map(policy -> policy.calculateFare(totalFare))
            .collect(Collectors.toUnmodifiableList());
    }

    private FareResponse getFareResponse(final Fare totalFare, final List<Fare> discountFares) {
        final Fare teenagerFare = discountFares.get(0);
        final Fare childFare = discountFares.get(1);
        return new FareResponse(totalFare.fare(), teenagerFare.fare(), childFare.fare());
    }
}
