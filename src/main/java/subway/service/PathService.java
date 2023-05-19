package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.dto.PathResponse;
import subway.persistence.repository.PathRepository;

@Service
public class PathService {
    private final PathRepository pathRepository;

    public PathService(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    public PathResponse getShortestPath(Long departureStationId, Long arrivalStationId) {
        Station departure = pathRepository.findStationById(departureStationId);
        Station arrival = pathRepository.findStationById(arrivalStationId);

        Sections sections = pathRepository.findAll();
        Subway subway = Subway.from(sections);
        SubwayGraph subwayGraph = SubwayGraph.from(subway);

        ShortestPath shortestPath = subwayGraph.getDijkstraShortestPath(departure, arrival);
        return PathResponse.of(shortestPath);
    }
}
