package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@Service
public class SectionService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionService(final StationRepository stationRepository, final LineRepository lineRepository,
            final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public void initialStations(final Long lineId, final Long upStationId, final Long downStationId,
            final int distance) {
        final Line line = lineRepository.findById(lineId).orElseThrow();
        final Station upStation = stationRepository.findById(upStationId).orElseThrow();
        final Station downStation = stationRepository.findById(downStationId).orElseThrow();

        sectionRepository.findAllByLine(line);
        line.initialStations(upStation, downStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addEndStation(final Long lineId, final Long sourceStationId, final Long targetStationId,
            final int distance) {
        final Line line = lineRepository.findById(lineId).orElseThrow();
        final Station sourceStation = stationRepository.findById(sourceStationId).orElseThrow();
        final Station targetStation = stationRepository.findById(targetStationId).orElseThrow();

        sectionRepository.findAllByLine(line);
        line.addEndStation(sourceStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void addMiddleStation(final Long lineId, final Long upStationId, final Long downStationId,
            final Long targetStationId, final int distance) {
        final Line line = lineRepository.findById(lineId).orElseThrow();
        final Station upStation = stationRepository.findById(upStationId).orElseThrow();
        final Station downStation = stationRepository.findById(downStationId).orElseThrow();
        final Station targetStation = stationRepository.findById(targetStationId).orElseThrow();

        sectionRepository.findAllByLine(line);
        line.addMiddleStation(upStation, downStation, targetStation, Distance.from(distance));
        sectionRepository.insert(line);
    }

    public void removeStation(final Long lineId, final Long targetStationId) {
        final Line line = lineRepository.findById(lineId).orElseThrow();
        final Station targetStation = stationRepository.findById(targetStationId).orElseThrow();

        sectionRepository.findAllByLine(line);
        line.removeStation(targetStation);
        sectionRepository.insert(line);
    }
}
