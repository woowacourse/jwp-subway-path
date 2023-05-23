package subway.application;

import static subway.application.StationNamesConvertor.convertToStationNamesByStations;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.DijkstraStrategy;
import subway.domain.Distance;
import subway.domain.Fee;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.DistanceDto;
import subway.dto.FeeDto;
import subway.dto.RouteDto;

@Service
public class RouteService {

    private final SectionDao sectionDao;
    private final SectionsMapper sectionsMapper;
    private final DijkstraStrategy dijkstraStrategy;

    public RouteService(final SectionDao sectionDao, final SectionsMapper sectionsMapper,
                        final DijkstraStrategy dijkstraStrategy) {
        this.sectionDao = sectionDao;
        this.sectionsMapper = sectionsMapper;
        this.dijkstraStrategy = dijkstraStrategy;
    }

    public RouteDto getFeeByStations(final String startStationName, final String endStationName) {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Sections sections = sectionsMapper.mapFrom(sectionEntities);

        Station startStation = new Station(startStationName);
        Station endStation = new Station(endStationName);

        List<Station> shortestPath = dijkstraStrategy.getShortestPath(sections, startStation, endStation);
        Distance distance = dijkstraStrategy.getShortestPathWeight(sections, startStation, endStation);

        Fee fee = Fee.toDistance(distance.getDistance());

        return new RouteDto(new DistanceDto(distance.getDistance()), new FeeDto(fee.getFee()),
                convertToStationNamesByStations(shortestPath));
    }

}
