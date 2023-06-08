package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Fare;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.dto.response.PathStationResponse;
import subway.repository.SectionRepository;
import subway.repository.ShortestPathFinder;
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

        PathFinder pathFinder = ShortestPathFinder.of(sections);
        Path path = pathFinder.findPath(sourceStation, targetStation);

        List<PathStationResponse> stationResponses = path.getPath().stream()
                .map(station -> new PathStationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, path.getDistance(), Fare.calculateFare(path.getDistance()));
    }
}
