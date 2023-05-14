package subway.application.section;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.SectionCreateRequest;

@Service
public class AttachStationService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;


    public AttachStationService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }


    public void createSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        final Line line = lineRepository.findById(lineId);
        final Section section = createBy(lineId, sectionCreateRequest);
        Sections sections = new Sections(sectionRepository.findAllByLineId(line.getId()));

        sections.addSection(section);
        sectionRepository.saveSection(lineId, sections.getSections());
    }

    private Section createBy(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        final Station upStation = stationRepository.findByName(new Station(sectionCreateRequest.getUpStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        final Station downStation = stationRepository.findByName(new Station(sectionCreateRequest.getDownStationName()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return new Section(
                lineId,
                upStation,
                downStation,
                sectionCreateRequest.getDistance()
        );
    }
}
