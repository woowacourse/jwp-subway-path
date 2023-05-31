package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Path;
import subway.domain.Section;
import subway.domain.Station;
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

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        Path path = Path.of(stations, sections);

        List<String> shortestPath = path.getDijkstraShortestPath(
                pathRequest.getSourceStation(),
                pathRequest.getTargetStation()
        );

        int shortestPathDistance = path.getDijkstraShortestPathDistance(pathRequest.getSourceStation(),
                pathRequest.getTargetStation());

        int fare = path.calculateFare(shortestPathDistance);

        return new PathResponse(shortestPath, shortestPathDistance, fare);
    }
}
