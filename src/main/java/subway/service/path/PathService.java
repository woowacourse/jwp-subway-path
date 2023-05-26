package subway.service.path;

import org.springframework.stereotype.Service;
import subway.service.path.domain.FeePolicy;
import subway.service.path.domain.PathRouter;
import subway.service.path.domain.SectionEdge;
import subway.service.path.dto.ShortestPath;
import subway.service.section.domain.Section;
import subway.service.section.dto.PathResult;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;
import subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final FeePolicy feePolicy;
    private final PathRouter pathRouter;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository, FeePolicy feePolicy, PathRouter pathRouter) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.feePolicy = feePolicy;
        this.pathRouter = pathRouter;
    }

    public PathResult calculateShortestPathFee(long sourceStationId, long targetStationId) {
        List<Section> sections = sectionRepository.findAll();
        Station source = stationRepository.findById(sourceStationId);
        Station target = stationRepository.findById(targetStationId);

        ShortestPath shortestPath = pathRouter.findShortestPath(sections, source, target);
        List<SectionEdge> edges = shortestPath.getEdges();
        int fee = feePolicy.calculateFee(edges);

        List<Station> pathStations = shortestPath.getStations();
        List<StationResponse> stationResponses = pathStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new PathResult(fee, stationResponses);
    }

}
