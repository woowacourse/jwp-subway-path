package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Distance;
import subway.domain.Fee;
import subway.domain.Route;
import subway.domain.Sections;
import subway.domain.ShortestPathAlgorithmStrategy;
import subway.domain.Station;

@Service
public class RouteService {

    private final SectionDao sectionDao;
    private final SectionsMapper sectionsMapper;
    private final ShortestPathAlgorithmStrategy shortestPathAlgorithmStrategy;

    public RouteService(final SectionDao sectionDao, final SectionsMapper sectionsMapper,
                        final ShortestPathAlgorithmStrategy shortestPathAlgorithmStrategy) {
        this.sectionDao = sectionDao;
        this.sectionsMapper = sectionsMapper;
        this.shortestPathAlgorithmStrategy = shortestPathAlgorithmStrategy;
    }

    public Route getFeeByStations(final String startStationName, final String endStationName) {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Sections sections = sectionsMapper.mapFrom(sectionEntities);

        Station startStation = new Station(startStationName);
        Station endStation = new Station(endStationName);

        List<Station> shortestPath = shortestPathAlgorithmStrategy.getShortestPath(sections, startStation, endStation);
        Distance distance = shortestPathAlgorithmStrategy.getShortestPathWeight(sections, startStation, endStation);

        Fee fee = Fee.toDistance(distance.getDistance());

        return new Route(shortestPath, distance, fee);
    }

}
