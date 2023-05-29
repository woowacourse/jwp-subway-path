package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.graph.SubwayGraph;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.graph.JgraphtGraph;
import subway.domain.fare.Fare;
import subway.domain.fare.FareCalculator;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.repository.PathRepository;

@Service
@Transactional
public class PathService {
    private final PathRepository pathRepository;
    private final FareCalculator fareCalculator;

    public PathService(final PathRepository pathRepository, final FareCalculator fareCalculator) {
        this.pathRepository = pathRepository;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(final PathRequest request) {
        Sections sections = pathRepository.findAllSections();
        return buildPathResponse(sections, request);
    }

    private PathResponse buildPathResponse(final Sections sections, final PathRequest request) {
        SubwayGraph graph = new JgraphtGraph();
        Station source = pathRepository.findStationById(request.getSourceStationId());
        Station destination = pathRepository.findStationById(request.getDestinationStationId());
        List<Station> path = graph.getPath(sections.getSections(), source, destination);
        int distance = graph.getWeight(sections.getSections(), source, destination);
        Fare fare = fareCalculator.calculate(distance);
        return PathResponse.of(path, distance, fare);
    }
}
