package subway.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.application.dto.ShortestPathInfoDto;
import subway.application.dto.ShortestPathsDto;
import subway.domain.fare.FareAmount;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Line;
import subway.domain.path.Path;
import subway.domain.path.ShortestPathCalculator;
import subway.domain.station.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Service
public class PathService {

    private static final int VALID_STATION_SIZE = 2;

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final ShortestPathCalculator calculator;
    private final FarePolicy farePolicy;

    public PathService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository,
            final ShortestPathCalculator shortestPathCalculator,
            final FarePolicy farePolicy
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.calculator = shortestPathCalculator;
        this.farePolicy = farePolicy;
    }

    public ShortestPathInfoDto findShortestPathInfo(final Long sourceStationId, final Long targetStationId) {
        validateUniqueStationId(sourceStationId, targetStationId);

        final Map<Long, Station> stations = findStations(sourceStationId, targetStationId);
        final List<Line> lines = lineRepository.findAll()
                .stream()
                .map(sectionRepository::findAllByLine)
                .collect(Collectors.toList());
        final Path path = calculator.findPath(lines,
                stations.get(sourceStationId),
                stations.get(targetStationId)
        );

        final ShortestPathsDto shortestPathsDto = ShortestPathsDto.from(path);
        final FareAmount fareAmount = farePolicy.calculate(path.calculateTotalDistance());

        return ShortestPathInfoDto.of(shortestPathsDto, fareAmount);
    }

    private Map<Long, Station> findStations(final Long sourceStationId, final Long targetStationId) {
        final Map<Long, Station> stations = stationRepository.findAllByIds(Set.of(sourceStationId, targetStationId));

        validateExistsStation(stations);

        return stations;
    }

    private void validateUniqueStationId(final Long sourceStationId, final Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발 역과 도착 역이 동일할 수 없습니다.");
        }
    }

    private void validateExistsStation(final Map<Long, Station> stations) {
        if (stations.size() != VALID_STATION_SIZE) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }
}
