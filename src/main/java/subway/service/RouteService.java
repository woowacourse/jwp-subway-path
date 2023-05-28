package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.FeeCalculator;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.feePolicy.AgePolicy;
import subway.domain.feePolicy.DistancePolicy;
import subway.dto.RouteResponse;
import subway.repository.SectionRepository;

@Service
@Transactional
public class RouteService {

    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public RouteService(final StationDao stationDao, final SectionRepository sectionRepository) {
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    public RouteResponse findShortestRoute(final Long startStationId, final Long endStationId, final int age) {
        Station startStation = stationDao.findById(startStationId);
        Station endStation = stationDao.findById(endStationId);
        Sections sections = sectionRepository.findAll();

        PathFinder pathFinder = new PathFinder(sections.getSections());
        Path shortesPath = pathFinder.findShortesPath(startStation, endStation);

        DistancePolicy distancePolicy = DistancePolicy.from(shortesPath.getDistance());
        AgePolicy agePolicy = AgePolicy.from(age);
        FeeCalculator feeCalculator = new FeeCalculator(distancePolicy, agePolicy);
        int totalFee = feeCalculator.calculate(shortesPath);

        return new RouteResponse(shortesPath.getStations(), shortesPath.getDistanceValue(), totalFee);
    }
}
