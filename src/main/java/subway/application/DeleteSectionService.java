package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Service
public class DeleteSectionService {

    private static final String NOT_EXISTS_STATION = "존재하지 않는 역입니다.";
    private static final String NOT_EXISTS_LINE = "존재하지 않는 노선입니다.";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public DeleteSectionService(
            final StationRepository stationRepository,
            final LineRepository lineRepository,
            final SectionRepository sectionRepository
    ) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void removeSection(final Long targetStationId, final Long lineId) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Line line = sectionRepository.findAllByLine(persistLine);
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));

        final Line deletedLine = line.deleteSection(targetStation);

        sectionRepository.insert(deletedLine);
    }
}
