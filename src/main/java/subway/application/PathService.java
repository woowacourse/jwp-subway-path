package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.path.Fee;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.repository.LineRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortPath(final PathRequest pathRequest) {
        Station departurestation = lineRepository.findStationByName(pathRequest.getDepartureStation(), pathRequest.getDepartureLine());
        Station arrivalStation = lineRepository.findStationByName(pathRequest.getArrivalStation(), pathRequest.getArrivalLine());
        Sections sections = lineRepository.readAllSection();

        PathFinder pathFinder = new PathFinder();
        Path shortestPath = pathFinder.findShortestPath(sections, departurestation, arrivalStation);
        Fee fee = shortestPath.calculateFee();

        return PathResponse.of(fee, shortestPath);
    }
}
