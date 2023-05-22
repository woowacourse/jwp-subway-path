package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class SubwayReadService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SubwayReadService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId);
    }

    public SingleLineSections findSingLineSectionsByLineId(Long lineId) {
        return sectionRepository.findAllByLineId(lineId);
    }

    public boolean exists(Long lineId) {
        return lineRepository.exists(lineId);
    }
}
