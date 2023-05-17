package subway.application;

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

    private static final String NOT_EXISTS_STATION = "존재하지 않는 역입니다.";
    private static final String NOT_EXISTS_LINE = "존재하지 않는 노선입니다.";

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
        validateStation(sourceStationId, targetStationId);

        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Line line = sectionRepository.findAllByLine(persistLine);
        final Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));

        line.createSection(sourceStation, targetStation, Distance.from(distance), direction);

        sectionRepository.insert(line);

        return CreateSectionDto.from(line);
    }

    private void validateStation(final Long sourceStationId, final Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("노선에 등록할 역은 동일한 역을 지정할 수 없습니다.");
        }
    }
}
