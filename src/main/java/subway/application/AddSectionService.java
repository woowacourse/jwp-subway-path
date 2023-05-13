package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Transactional
@Service
public class AddSectionService {

    private static final String NOT_EXISTS_STATION = "존재하지 않는 역입니다.";
    private static final String NOT_EXISTS_LINE = "존재하지 않는 노선입니다.";

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public AddSectionService(final StationRepository stationRepository,
                             final LineRepository lineRepository,
                             final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public void addInitialStations(final Long lineId, final Long upStationId, final Long downStationId,
                                   final int distance) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Line line = sectionRepository.findAllByLine(persistLine);

        line.addInitialStations(upStation, downStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addEndStation(final Long lineId, final Long sourceStationId, final Long targetStationId,
                              final int distance) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Line line = sectionRepository.findAllByLine(persistLine);

        line.addEndStation(sourceStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addMiddleStation(final Long lineId, final Long upStationId, final Long downStationId,
                                 final Long targetStationId, final int distance) {
        final Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_LINE));
        final Station upStation = stationRepository.findById(upStationId)
                .orElseThrow();
        final Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));
        final Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));

        final Line line = sectionRepository.findAllByLine(persistLine);
        line.addMiddleStation(upStation, downStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }
}
