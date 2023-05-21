package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.LineStatus;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.StationRequest;

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
        Optional<Line> findNullableLine = lineRepository.findByLineName(stationRequest.getLineName());
        if (LineStatus.of(findNullableLine) == LineStatus.INITIAL) {
            return saveInitialSection(stationRequest);
        }
        Line line = findNullableLine.get();
        Section sectionToAdd = getSectionFromRequest(stationRequest, line);

        Sections sections = new Sections(sectionRepository.findSectionsByLineId(line.getId()));
        validateSectionCanLink(sections, sectionToAdd);
        return saveAdditionalSection(sections, sectionToAdd);
    }

    private void validateSectionCanLink(Sections sections, Section sectionToAdd) {
        List<Section> sectionsContainUpStation = sections.findSectionsContainStation(sectionToAdd.getUpStation());
        List<Section> sectionsContainDownStation = sections.findSectionsContainStation(sectionToAdd.getDownStation());
        if (sectionsContainUpStation.isEmpty() && sectionsContainDownStation.isEmpty()) {
            throw new IllegalArgumentException("현재 등록된 역 중에 하나를 포함해야합니다.");
        }
    }

    private Long saveInitialSection(StationRequest stationRequest) {
        Line line = lineRepository.insert(new Line(null, stationRequest.getLineName()));
        Section sectionToAdd = getSectionFromRequest(stationRequest, line);
        Section insertedSection = sectionRepository.insert(sectionToAdd);
        return insertedSection.getLineId();
    }

    private Section getSectionFromRequest(StationRequest stationRequest, Line line) {
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        return new Section(null, upStation, downStation, distance);
    }

    private Long saveAdditionalSection(Sections sections, Section sectionToAdd) {
        Optional<Section> nullableSectionToModify = sections.findSectionContainSection(sectionToAdd);
        if (nullableSectionToModify.isEmpty()) {
            return insertToEnd(sectionToAdd);
        }
        return insertToMiddle(sectionToAdd, nullableSectionToModify.get());
    }

    private Long insertToEnd(Section sectionToAdd) {
        Section insertedSection = sectionRepository.insert(sectionToAdd);
        return insertedSection.getLineId();
    }

    private Long insertToMiddle(Section sectionToAdd, Section sectionToModify) {
        Section modifiedSection = sectionToModify.subtract(sectionToAdd);

        sectionRepository.insert(modifiedSection);
        sectionRepository.insert(sectionToAdd);
        sectionRepository.remove(sectionToModify);
        return sectionToAdd.getLineId();
    }
}
