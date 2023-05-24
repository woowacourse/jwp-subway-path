package subway.application;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.CreateSectionDto;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Service
public class CreateSectionService {

    private static final String NOT_EXISTS_LINE = "존재하지 않는 노선입니다.";
    private static final int VALID_STATION_SIZE = 2;

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public CreateSectionService(
            final StationRepository stationRepository,
            final LineRepository lineRepository,
            final SectionRepository sectionRepository
    ) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public CreateSectionDto addSection(
            final Long lineId,
            final Long sourceStationId,
            final Long targetStationId,
            final Direction direction,
            final int distance
    ) {
        final Map<Long, Station> stations = findStations(sourceStationId, targetStationId);
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Line line = sectionRepository.findAllByLine(persistLine);

        final Line createdLine = line.createSection(stations.get(sourceStationId), stations.get(targetStationId),
                Distance.from(distance), direction);
        sectionRepository.insert(createdLine);

        return CreateSectionDto.from(createdLine);
    }

    private Map<Long, Station> findStations(final Long sourceStationId, final Long targetStationId) {
        validateUniqueStationId(sourceStationId, targetStationId);

        final Map<Long, Station> stations = stationRepository.findAllByIds(Set.of(sourceStationId, targetStationId));

        validateExistsStation(stations);

        return stations;
    }

    private void validateUniqueStationId(final Long sourceStationId, final Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("노선에 등록할 역은 동일한 역을 지정할 수 없습니다.");
        }
    }

    private void validateExistsStation(final Map<Long, Station> stations) {
        if (stations.size() != VALID_STATION_SIZE) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }
}
