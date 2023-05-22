package subway.application;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(final Long upStationId, final Long downStationId) {
        final ShortestPath graph = createGraph();

        final Station upStation = stationRepository.findById(upStationId);
        final Station downStation = stationRepository.findById(downStationId);
        final List<Station> shortestPath = graph.findShortestPath(upStation, downStation);
        final int distance = graph.getDistance(upStation, downStation);

        final List<StationResponse> stationResponses = shortestPath.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance, Fare.calculate(distance));
    }

    private ShortestPath createGraph() {
        final List<Section> sections = lineRepository.findAll().stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return new ShortestPath(new Sections(sections));
    }
}
