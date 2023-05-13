package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Transactional
@Service
public class RemoveSectionService {

    private static final String NOT_EXISTS_STATION = "존재하지 않는 역입니다.";
    private static final String NOT_EXISTS_LINE = "존재하지 않는 노선입니다.";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public RemoveSectionService(final StationRepository stationRepository,
                                final LineRepository lineRepository,
                                final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public void removeAllStation(final Long lineId) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Line line = sectionRepository.findAllByLine(persistLine);

        line.removeAllStation();
        sectionRepository.insert(line);
    }

    public void removeEndStation(final Long lineId, final Long targetStationId) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Line line = sectionRepository.findAllByLine(persistLine);

        line.removeEndStation(targetStation);
        sectionRepository.insert(line);
    }

    public void removeMiddleStation(final Long lineId, final Long targetStationId) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Line line = sectionRepository.findAllByLine(persistLine);

        line.removeMiddleStation(targetStation);
        sectionRepository.insert(line);
    }
}
