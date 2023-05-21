package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.path.Fee;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortPath(final PathRequest pathRequest) {
        Station departurestation = stationRepository.findByName(pathRequest.getDepartureStation(), pathRequest.getDepartureLine());
        Station arrivalStation = stationRepository.findByName(pathRequest.getArrivalStation(), pathRequest.getArrivalLine());
        Sections sections = sectionRepository.readAllSection();

        PathFinder pathFinder = new PathFinder();
        Path shortestPath = pathFinder.findShortestPath(sections, departurestation, arrivalStation);
        Fee fee = shortestPath.calculateFee();

        return PathResponse.of(fee, shortestPath);
    }
}
