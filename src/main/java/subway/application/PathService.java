package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.SubwayGraph;
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
        Station source = pathRepository.findStationById(request.getSourceStationId());
        Station destination = pathRepository.findStationById(request.getDestinationStationId());
        SubwayGraph graph = new SubwayGraph(sections.getSections());
        List<Station> path = graph.getPath(source, destination);
        int distance = graph.getWeight(source, destination);
        Fare fare = fareCalculator.calculate(distance);
        return PathResponse.of(path, distance, fare);
    }
}
