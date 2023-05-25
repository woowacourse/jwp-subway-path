package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.response.ShortestPathResponse;
import subway.domain.Distance;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.fee.Fee;
import subway.domain.fee.FeeCalculator;
import subway.domain.path.Path;
import subway.domain.path.ShortestPathFinder;
import subway.entity.SectionDetailEntity;
import subway.repository.SectionDao;
import subway.repository.StationDao;

import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final ShortestPathFinder shortestPathFinder;
    private final FeeCalculator feeCalculator;

    public PathService(final StationDao stationDao, final SectionDao sectionDao,
                       final ShortestPathFinder shortestPathFinder, final FeeCalculator feeCalculator) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.shortestPathFinder = shortestPathFinder;
        this.feeCalculator = feeCalculator;
    }

    public ShortestPathResponse findShortestPath(final String startStationName, final String endStationName) {
        final List<SectionDetailEntity> allSectionEntities = sectionDao.findSectionDetail();
        final Station startStation = makeStationByName(startStationName);
        final Station endStation = makeStationByName(endStationName);
        final Sections sections = Sections.createByDetailEntity(allSectionEntities);
        final Path path = shortestPathFinder.find(sections, startStation, endStation);
        final Distance distance = path.getTotalDistance();
        final Fee fee = feeCalculator.calculate(distance);
        return ShortestPathResponse.of(distance, fee, path);
    }

    private Station makeStationByName(final String name) {
        final long stationId = stationDao.findIdByName(name);
        return new Station(stationId, name);
    }
}
