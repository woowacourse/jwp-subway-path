package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.FeeCalculator;
import subway.domain.Line;
import subway.domain.PathFinder;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.RouteResponse;
import subway.repository.SectionRepository;
import java.util.List;

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
        List<Station> stations = pathFinder.findShortestPath(startStation, endStation);
        double distance = pathFinder.calculateShortestDistance(startStation, endStation);
        List<Line> passLines = pathFinder.findPassLine(startStation, endStation);

        FeeCalculator feeCalculator = new FeeCalculator();
        int fee = feeCalculator.calculate(distance);
        int extraFeeByLine = feeCalculator.addExtraFeeByLine(fee, passLines);
        int totalFee = feeCalculator.discountByAge(extraFeeByLine, age);

        return new RouteResponse(stations, distance, totalFee);
    }
}
