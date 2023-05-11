package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.ui.dto.StationResponse;

import java.util.List;

@Service
public class SectionService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        final Section section = Section.of(
                sectionCreateRequest.getUpStation(),
                sectionCreateRequest.getDownStation(),
                sectionCreateRequest.getDistance()
        );

        final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
        sections.addSection(section);
        sectionRepository.createSection(lineId, sections.getSections());
    }

    public void deleteSection(final Long lineId, final SectionDeleteRequest sectionDeleteRequest) {
        final Section section = Section.of(
                sectionDeleteRequest.getUpStation(),
                sectionDeleteRequest.getDownStation()
        );

        sectionRepository.deleteBySection(lineId, section);
    }

    public List<StationResponse> findAllByLine(final Long lineId) {
        final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));

        return StationResponse.of(sections.getSortedStations());
    }
}
