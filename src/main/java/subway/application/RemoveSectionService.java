package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Transactional
@Service
public class RemoveSectionService {

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
        final Line line = lineRepository.findById(lineId);

        sectionRepository.findAllByLine(line);
        line.removeAllStation();
        sectionRepository.insert(line);
    }

    public void removeEndStation(final Long lineId, final Long targetStationId) {
        final Line line = lineRepository.findById(lineId);
        final Station targetStation = stationRepository.findById(targetStationId);

        sectionRepository.findAllByLine(line);
        line.removeEndStation(targetStation);
        sectionRepository.insert(line);
    }

    public void removeMiddleStation(final Long lineId, final Long targetStationId) {
        final Line line = lineRepository.findById(lineId);
        final Station targetStation = stationRepository.findById(targetStationId);

        sectionRepository.findAllByLine(line);
        line.removeMiddleStation(targetStation);
        sectionRepository.insert(line);
    }
}
