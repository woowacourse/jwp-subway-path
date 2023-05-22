package subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fare.Fare;
import subway.domain.fare.FareCalculator;
import subway.dto.PathSearchRequest;
import subway.dto.PathSearchResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final FareCalculator fareCalculator;

    public PathService(final SectionDao sectionDao, final FareCalculator fareCalculator) {
        this.sectionDao = sectionDao;
        this.fareCalculator = fareCalculator;
    }

    public PathSearchResponse getShortestPath(final PathSearchRequest pathSearchRequest) {
        final List<Section> sections = sectionDao.findAll();
        final PathFinder pathFinder = new PathFinder(sections);
        final Station departureStation = new Station(pathSearchRequest.getDepartureStationId());
        final Station arrivalStation = new Station(pathSearchRequest.getArrivalStationId());
        final Path path = pathFinder.findShortestPath(departureStation, arrivalStation);
        final Fare fare = fareCalculator.calculate(path.getDistance());
        return new PathSearchResponse(path.getStations(), path.getDistance().getValue(), fare.getValue());
    }
}
