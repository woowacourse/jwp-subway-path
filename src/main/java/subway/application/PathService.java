package subway.application;

import org.springframework.stereotype.Service;
import subway.application.repository.SectionRepository;
import subway.domain.ChargeCalculator;
import subway.domain.Graph;
import subway.domain.Sections;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final Graph graph;
    private final ChargeCalculator chargeCalculator;

    public PathService(final SectionRepository sectionRepository, final Graph graph, final ChargeCalculator chargeCalculator) {
        this.sectionRepository = sectionRepository;
        this.graph = graph;
        this.chargeCalculator = chargeCalculator;
    }

    public PathResponse findShortestPath(final PathRequest pathRequest) {
        final Sections sections = sectionRepository.findAllSections();
        graph.setGraph(sections);
        final List<String> shortestPathInfo = graph.findShortestPathInfo(pathRequest.getStartStation(), pathRequest.getEndStation());
        final Long shortestDistance = graph.findShortestDistance(pathRequest.getStartStation(), pathRequest.getEndStation());
        final Long charge = chargeCalculator.calculateCharge(shortestDistance);
        return new PathResponse(shortestPathInfo, shortestDistance, charge);
    }
}
