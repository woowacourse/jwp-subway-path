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
        final Line line = lineRepository.findById(lineId);
        final Station upStation = stationRepository.findById(upStationId);
        final Station downStation = stationRepository.findById(downStationId);

        sectionRepository.findAllByLine(line);
        line.addInitialStations(upStation, downStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addEndStation(final Long lineId, final Long sourceStationId, final Long targetStationId,
                              final int distance) {
        final Line line = lineRepository.findById(lineId);
        final Station sourceStation = stationRepository.findById(sourceStationId);
        final Station targetStation = stationRepository.findById(targetStationId);

        sectionRepository.findAllByLine(line);
        line.addEndStation(sourceStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addMiddleStation(final Long lineId, final Long upStationId, final Long downStationId,
                                 final Long targetStationId, final int distance) {
        final Line line = lineRepository.findById(lineId);
        final Station upStation = stationRepository.findById(upStationId);
        final Station downStation = stationRepository.findById(downStationId);
        final Station targetStation = stationRepository.findById(targetStationId);

        sectionRepository.findAllByLine(line);
        line.addMiddleStation(upStation, downStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }
}
