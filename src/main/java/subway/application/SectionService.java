package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.SectionGraph;
import subway.domain.Station;
import subway.persistence.repository.SectionRepository;
import subway.ui.request.PathRequest;
import subway.ui.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionRepository serviceRepository;

    public SectionService(final SectionRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<StationResponse> findPath(final PathRequest pathRequest) {
        final SectionGraph sectionGraph = serviceRepository.findAll();
        final List<Station> shortestPathStations = sectionGraph.findShortestPath(
                pathRequest.getStartStationName(),
                pathRequest.getEndStationName()
        );
        return shortestPathStations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
