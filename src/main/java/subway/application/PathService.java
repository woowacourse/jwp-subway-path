package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.domain.Subway;
import subway.domain.SubwayGraph;
import subway.dto.ShortestPathSelectResponse;
import subway.dto.StationSelectResponse;
import subway.exception.station.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public ShortestPathSelectResponse findShortestPath(final Long sourceStationId, final Long targetStationId) {
        final Subway subway = new Subway(lineRepository.findAll());
        final SubwayGraph subwayGraph = SubwayGraph.from(subway);

        final Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new StationNotFoundException("출발 역이 존재하지 않습니다."));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new StationNotFoundException("도착 역이 존재하지 않습니다."));

        final List<Station> shortestPath = subwayGraph.findShortestPath(sourceStation, targetStation);
        final int shortestDistance = subwayGraph.calculateShortestDistance(sourceStation, targetStation);
        // TODO: 2023/05/21 요금 계산

        final List<StationSelectResponse> pathDto = shortestPath.stream()
                .map(station -> new StationSelectResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
        return new ShortestPathSelectResponse(pathDto, shortestDistance, 1250);
    }
}
