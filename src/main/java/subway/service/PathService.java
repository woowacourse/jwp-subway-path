package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.exception.OptionalHasNoStationException;
import subway.domain.Path.Path;
import subway.domain.Path.PathFinder;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.FarePolicy;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;
import subway.dto.DtoMapper;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.persistence.dao.StationDao;

@Service
@Transactional
public class PathService {

    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public PathService(StationDao stationDao, SectionRepository sectionRepository) {
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(PathRequest request) {
        Station source = stationDao.findById(request.getSourceId())
                .orElseThrow(OptionalHasNoStationException::new);
        Station destination = stationDao.findById(request.getDestinationId())
                .orElseThrow(OptionalHasNoStationException::new);

        PathFinder pathFinder = PathFinder.from(sectionRepository.readAllSections(), DistanceFarePolicy.getInstance());

        Path shortestPath = Path.from(
                pathFinder.findShortestPath(source, destination),
                pathFinder.findShortestDistance(source, destination),
                pathFinder.calculateFare(source, destination)
        );

        return DtoMapper.convertToPathResponse(shortestPath);
    }
}
