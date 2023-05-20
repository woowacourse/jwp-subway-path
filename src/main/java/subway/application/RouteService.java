package subway.application;

import static subway.application.mapper.SectionMapper.createSubwayLines;
import static subway.application.mapper.SectionMapper.getAllSections;
import static subway.application.mapper.SectionMapper.getSectionsByLineId;
import static subway.application.mapper.StationMapper.createStationResponses;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.domain.fare.ExtraFare;
import subway.domain.fare.Fare;
import subway.domain.fare.discount.DiscountFarePolicyComposite;
import subway.domain.fare.normal.FarePolicyComposite;
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
    private final FarePolicyComposite farePolicies;
    private final DiscountFarePolicyComposite discountFarePolicies;

    public RouteService(final GraphProvider graphProvider,
                        final LineRepository lineRepository,
                        final StationRepository stationRepository,
                        final FarePolicyComposite farePolicies,
                        final DiscountFarePolicyComposite discountFarePolicies) {
        this.graphProvider = graphProvider;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicies = farePolicies;
        this.discountFarePolicies = discountFarePolicies;
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
        final Fare fare = farePolicies.getTotalFare(shortestDistance);
        final Fare totalFare = fare.add(lineExtraFare.fare());
        final List<Fare> discountFare = discountFarePolicies.getDiscountFares(totalFare);

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

    private FareResponse getFareResponse(final Fare totalFare, final List<Fare> discountFares) {
        final Fare teenagerFare = discountFares.get(0);
        final Fare childFare = discountFares.get(1);
        return new FareResponse(totalFare.fare(), teenagerFare.fare(), childFare.fare());
    }
}
