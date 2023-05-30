package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.customer.Customer;
import subway.domain.fare.FareCalculator;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.PathResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final FareCalculator fareCalculator;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository, FareCalculator fareCalculator) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findShortestPath(Long startStationId, Long endStationId, int age) {
        List<Section> allSections = sectionRepository.findAllSections();
        Station startStation = stationRepository.findStationById(startStationId);
        Station endStation = stationRepository.findStationById(endStationId);

        Path path = PathFinder.findPath(new Sections(allSections), startStation, endStation);
        List<String> stationNames = path.getStationNames();
        int totalDistance = path.getDistance();

        int fare = fareCalculator.calculate(totalDistance, path.getLinesToUse());
        Customer customer = new Customer(age);
        fare = customer.getDiscountedPrice(fare);

        return new PathResponse(stationNames, totalDistance, fare);
    }
}
