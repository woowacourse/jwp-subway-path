package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.dto.request.SectionCreateRequest;
import subway.ui.dto.request.SectionDeleteRequest;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(SectionCreateRequest request) {
        Long lineId = request.getLineId();
        Line line = findLine(lineId);

        Station leftStation = findStation(request.getLeftStationName());
        Station rightStation = findStation(request.getRightStationName());
        Distance distance = new Distance(request.getDistance());
        Section section = new Section(leftStation, rightStation, distance);

        line.addSection(section);
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.saveAllByLineId(lineId, line.getSections());
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest request) {
        Long lineId = request.getLineId();
        Line line = findLine(lineId);
        Station station = findStation(request.getStationName());

        line.deleteSection(station);
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.saveAllByLineId(lineId, line.getSections());
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Station findStation(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }
}
