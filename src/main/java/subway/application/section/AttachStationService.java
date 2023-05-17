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
import subway.ui.dto.request.SectionCreateRequest;

import java.util.Optional;

@Service
@Transactional
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
        final Optional<Line> optionalLine = lineRepository.findById(lineId);
        if (optionalLine.isEmpty()) {
            throw new IllegalArgumentException("노선이 없습니다.");
        }
        final Section section = createBy(lineId, sectionCreateRequest);
        Sections sections = new Sections(sectionRepository.findAllByLineId(optionalLine.get().getId()));

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
