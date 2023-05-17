package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.SectionDeleteRequest;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetachStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public DetachStationService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public void deleteStation(final Long lineId, final SectionDeleteRequest sectionDeleteRequest) {
        final Optional<Line> optionalLine = lineRepository.findById(lineId);
        if (optionalLine.isEmpty()) {
            throw new IllegalArgumentException("노선이 없습니다");
        }
        final Station station = stationRepository.findByName(new Station(sectionDeleteRequest.getStationName()))
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));

        final List<Section> findSections = sectionRepository.findAllByLineId(optionalLine.get().getId());
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }
        Sections sections = new Sections(findSections);
        sections.remove(station);

        sectionRepository.saveSection(lineId, sections.getSections());
    }
}
