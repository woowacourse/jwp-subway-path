package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Fare;
import subway.domain.Path;
import subway.domain.Section;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;
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

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Section> sections = sectionRepository.findAll();
        Station sourceStation = stationRepository.findByName(pathRequest.getSourceStation());
        Station targetStation = stationRepository.findByName(pathRequest.getTargetStation());

        Path path = ShortestPath.of(sections);
        Fare fare = new Fare();

        List<Station> shortestPath = path.findPath(sourceStation, targetStation);
        int shortestPathDistance = path.getDistance(sourceStation, targetStation);
        int shortestPathFare = fare.calculateFare(shortestPathDistance);

        List<StationResponse> stationResponses = shortestPath.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, shortestPathDistance, shortestPathFare);
    }
}
