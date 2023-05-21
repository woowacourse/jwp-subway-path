package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Subway;
import subway.domain.SubwayGraph;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Station;
import subway.dto.path.ShortestPathSelectResponse;
import subway.dto.station.StationSelectResponse;
import subway.exception.station.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FarePolicy farePolicy;

    public PathService(final LineRepository lineRepository,
                       final StationRepository stationRepository,
                       final FarePolicy farePolicy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicy = farePolicy;
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
        final Fare fare = new Fare(farePolicy, shortestDistance);

        return new ShortestPathSelectResponse(
                StationSelectResponse.from(shortestPath),
                shortestDistance,
                fare.getValue()
        );
    }
}
