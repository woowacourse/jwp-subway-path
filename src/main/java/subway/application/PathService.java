package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.FareCalculator;
import subway.domain.graph.Path;
import subway.domain.graph.SubwayGraph;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.repository.PathRepository;

@Service
@Transactional
public class PathService {
    private final PathRepository pathRepository;
    private final FareCalculator fareCalculator;
    private final SubwayGraph subwayGraph;

    public PathService(final PathRepository pathRepository, final FareCalculator fareCalculator,
                       final SubwayGraph subwayGraph) {
        this.pathRepository = pathRepository;
        this.fareCalculator = fareCalculator;
        this.subwayGraph = subwayGraph;
    }

    public PathResponse findPath(final PathRequest request) {
        Sections sections = pathRepository.findAllSections();
        Station source = pathRepository.findStationById(request.getSourceStationId());
        Station destination = pathRepository.findStationById(request.getDestinationStationId());
        Path path = Path.of(source, destination, subwayGraph, sections, fareCalculator);
        return PathResponse.from(path);
    }
}
