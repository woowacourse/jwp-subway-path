package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Distance;
import subway.domain.Fee;
import subway.domain.Route;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.DistanceDto;
import subway.dto.FeeDto;
import subway.dto.RouteDto;

@Service
public class RouteService {

    private final SectionDao sectionDao;
    private final SectionsMapper sectionsMapper;

    public RouteService(final SectionDao sectionDao, final SectionsMapper sectionsMapper) {
        this.sectionDao = sectionDao;
        this.sectionsMapper = sectionsMapper;
    }

    public RouteDto getFeeByStations(final String startStationName, final String endStationName) {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Sections sections = sectionsMapper.mapFrom(sectionEntities);

        Station startStation = new Station(startStationName);
        Station endStation = new Station(endStationName);

        Route route = new Route(sections.getSections());

        List<Station> shortestPath = route.getPath(startStation, endStation);
        Distance distance = new Distance(route.getPathWeight(startStation, endStation));

        Fee fee = Fee.toDistance(distance.getDistance());

        return new RouteDto(new DistanceDto(distance.getDistance()), new FeeDto(fee.getFee()),
                convertToStationNamesByStations(shortestPath));
    }

    private List<String> convertToStationNamesByStations(final List<Station> shortestPath) {
        return shortestPath.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}
