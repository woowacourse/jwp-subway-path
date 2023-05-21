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
import subway.domain.station.StationRepository;
import subway.dto.StationRequest;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long saveSection(StationRequest stationRequest) {
        Optional<Line> findNullableLine = lineRepository.findByLineName(stationRequest.getLineName());
        if (LineStatus.of(findNullableLine) == LineStatus.INITIAL) {
            return saveInitialSection(stationRequest);
        }
        Line line = findNullableLine.get();
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        Section sectionToAdd = new Section(null, upStation, downStation, distance);

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
        Station upStation = new Station(null, stationRequest.getUpStationName(), line);
        Station downStation = new Station(null, stationRequest.getDownStationName(), line);
        Distance distance = new Distance(stationRequest.getDistance());
        Section sectionToAdd = new Section(null, upStation, downStation, distance);
        Section insertedSection = sectionRepository.insert(sectionToAdd);
        return insertedSection.getLineId();
    }

    private Long saveAdditionalSection(Sections sections, Section sectionToAdd) {
        Optional<Section> nullableSectionToModify = sections.findSectionContainSection(sectionToAdd);
        if (nullableSectionToModify.isEmpty()) {
            Section insertedSection = sectionRepository.insert(sectionToAdd);
            return insertedSection.getLineId();
        }
        Section sectionToModify = nullableSectionToModify.get();
        Section modifiedSection = sectionToModify.subtract(sectionToAdd);

        sectionRepository.insert(modifiedSection);
        sectionRepository.insert(sectionToAdd);
        sectionRepository.remove(sectionToModify);
        return sectionToAdd.getLineId();
    }

    public Long deleteStationById(Long id) {
        Station stationToDelete = stationRepository.findStationById(id);
        Line targetLine = stationToDelete.getLine();
        Sections sections = new Sections(sectionRepository.findSectionsByLineId(targetLine.getId()));
        List<Section> sectionsToCombine = sections.findSectionsContainStation(stationToDelete);
        if (sectionsToCombine.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
        stationRepository.remove(stationToDelete);

        if (sections.getSections().size() == 1) {
            lineRepository.remove(targetLine);
            return targetLine.getId();
        }

        if (sectionsToCombine.size() == 1) {
            return targetLine.getId();
        }

        Section combinedSection = combineSections(sectionsToCombine);
        Section insertedSection = sectionRepository.insert(combinedSection);
        return insertedSection.getLineId();
    }

    private Section combineSections(List<Section> sectionsToCombine) {
        Section section1 = sectionsToCombine.get(0);
        Section section2 = sectionsToCombine.get(1);
        return section1.combine(section2);
    }
}