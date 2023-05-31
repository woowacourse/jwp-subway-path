package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.exception.CannotLinkException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaveSectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SaveSectionService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long saveSection(StationRequest stationRequest) {
        Long lineId = stationRequest.getLineId();
        Line line = lineRepository.findLineById(lineId);
        Section sectionToAdd = getSectionFromRequest(stationRequest, line);
        Sections sections = new Sections(sectionRepository.findSectionsByLineId(line.getId()));
        validateSectionCanLink(sections, sectionToAdd);
        return saveAccordingRule(sectionToAdd, sections);
    }

    private Section getSectionFromRequest(StationRequest stationRequest, Line line) {
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        return new Section(null, upStation, downStation, distance);
    }

    private void validateSectionCanLink(Sections sections, Section sectionToAdd) {
        if (sections.getSections().isEmpty()) {
            return;
        }
        List<Section> sectionsContainUpStation = sections.findSectionsContainStation(sectionToAdd.getUpStation());
        List<Section> sectionsContainDownStation = sections.findSectionsContainStation(sectionToAdd.getDownStation());
        if (sectionsContainUpStation.isEmpty() && sectionsContainDownStation.isEmpty()) {
            throw new CannotLinkException();
        }
    }

    private Long saveAccordingRule(Section sectionToAdd, Sections sections) {
        Optional<Section> nullableSectionToModify = sections.findSectionContainSection(sectionToAdd);
        if (nullableSectionToModify.isEmpty()) {
            Section insertedSection = sectionRepository.insert(sectionToAdd);
            return insertedSection.getLineId();
        }
        return insertToMiddle(sectionToAdd, nullableSectionToModify.get());
    }

    private Long insertToMiddle(Section sectionToAdd, Section sectionToModify) {
        Section modifiedSection = sectionToModify.subtract(sectionToAdd);

        sectionRepository.insert(modifiedSection);
        sectionRepository.insert(sectionToAdd);
        sectionRepository.remove(sectionToModify);

        return sectionToAdd.getLineId();
    }
}
