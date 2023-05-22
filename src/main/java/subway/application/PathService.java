package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Line;
import subway.domain.line.SubwayMap;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.path.SectionEdge;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.DistanceResponse;
import subway.dto.FareResponse;
import subway.dto.LineAndSectionsResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FarePolicy farePolicy;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository, final FarePolicy farePolicy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicy = farePolicy;
    }

    public PathResponse findShortestPath(final PathRequest pathRequest) {
        final SubwayMap subwayMap = new SubwayMap(lineRepository.findLines());
        final PathFinder pathFinder = new PathFinder(subwayMap);
        final Station from = stationRepository.findStationById(pathRequest.getFromStationId());
        final Station to = stationRepository.findStationById(pathRequest.getToStationId());
        final Path path = pathFinder.findShortest(from, to);
        final int fare = farePolicy.calculate(path.getDistance());
        return new PathResponse(toResponses(path.getSectionEdges()), DistanceResponse.of(path.getDistance()), new FareResponse(fare));
    }

    private List<LineAndSectionsResponse> toResponses(final List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .collect(Collectors.groupingBy(SectionEdge::getLineId))
                .entrySet().stream()
                .map(entry -> {
                    final Long lineId = entry.getKey();
                    final Line line = lineRepository.findLineById(lineId);
                    final List<Section> sections = entry.getValue().stream()
                            .map(SectionEdge::toSection)
                            .collect(Collectors.toUnmodifiableList());
                    return LineAndSectionsResponse.of(line, sections);
                }).collect(Collectors.toUnmodifiableList());
    }
}
