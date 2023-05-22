package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.farePolicy.AgeDiscountPolicy;
import subway.domain.farePolicy.DistanceProportionalPolicy;
import subway.domain.farePolicy.FarePolicyChain;
import subway.domain.farePolicy.LineAdditionalPolicy;
import subway.domain.general.Money;
import subway.dto.PassengerDto;
import subway.dto.response.PathAndFareResponse;

import java.util.List;

@Service
public class PathFareService {

    private final PathService pathService;
    private final SectionService sectionService;

    private final DistanceProportionalPolicy distanceProportionalPolicy;
    private final AgeDiscountPolicy ageDiscountPolicy;
    private final LineAdditionalPolicy lineAdditionalPolicy;

    public PathFareService(PathService pathService, SectionService sectionService, DistanceProportionalPolicy distanceProportionalPolicy,
                           AgeDiscountPolicy ageDiscountPolicy, LineAdditionalPolicy lineAdditionalPolicy) {
        this.pathService = pathService;
        this.sectionService = sectionService;
        this.distanceProportionalPolicy = distanceProportionalPolicy;
        this.ageDiscountPolicy = ageDiscountPolicy;
        this.lineAdditionalPolicy = lineAdditionalPolicy;
    }

    public PathAndFareResponse calculateRouteFare(Long startId, Long endId, PassengerDto passengerDto) {
        FarePolicyChain farePolicyChain = new FarePolicyChain(new FarePolicy[]{distanceProportionalPolicy, ageDiscountPolicy, lineAdditionalPolicy});

        List<Section> path = pathService.findShortestPath(startId, endId);
        List<Sections> allSections = sectionService.getAllSections();

        Money fare = farePolicyChain.applyPolicy(Money.of(0), allSections, path, passengerDto);
        List<Station> pathStations = new Sections(0, path).getStationsInOrder();

        return new PathAndFareResponse(pathStations, fare.getMoney());
    }
}
