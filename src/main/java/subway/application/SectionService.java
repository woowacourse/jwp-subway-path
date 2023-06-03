package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.SectionRequest;
import subway.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        checkIfExistLine(lineId);
        checkIfExistStation(sectionRequest.getUpStationId());
        checkIfExistStation(sectionRequest.getDownStationId());
        SectionEntity sectionEntity = new SectionEntity(lineId, sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(), sectionRequest.getDistance());

        if (isEmptyLine(lineId)) {
            sectionDao.insert(sectionEntity);
            return;
        }
        saveSectionWhenLineIsNotEmpty(lineId, sectionEntity);
    }

    private boolean isEmptyLine(Long lineId) {
        Optional<List<SectionEntity>> byLineId = sectionDao.findByLineId(lineId);
        return byLineId.map(List::isEmpty)
                       .orElse(true);
    }

    private void saveSectionWhenLineIsNotEmpty(Long lineId, SectionEntity sectionToAdd) {
        List<Section> findSections = sectionDao.findSectionsByLineId(lineId)
                                               .stream()
                                               .map(Section::from)
                                               .collect(Collectors.toList());
        Set<Section> sectionsSnapshot = Set.copyOf(findSections);

        Sections sections = new Sections(findSections);

        Station upStation = convertToStation(sectionToAdd.getUpStationId());
        Station downStation = convertToStation(sectionToAdd.getDownStationId());
        sections.addSection(Section.of(sectionToAdd.getId(), upStation, downStation, sectionToAdd.getDistance()));

        updateSection(lineId, sectionsSnapshot, sections);
    }

    private void updateSection(Long lineId, Set<Section> sectionsSnapshot, Sections sections) {
        Set<Section> updateSections = Set.copyOf(sections.getSections());

        Set<Section> deleteSections = getDifference(sectionsSnapshot, updateSections);
        Set<Section> insertSections = getDifference(updateSections, sectionsSnapshot);

        sectionDao.deleteAll(convertToSectionEntities(deleteSections, lineId));
        sectionDao.insertAll(convertToSectionEntities(insertSections, lineId));
    }

    private Station convertToStation(Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                                                .orElseThrow(() -> new NotFoundException("해당 역을 찾을 수 없습니다."));
        return Station.from(stationEntity);
    }

    private Set<Section> getDifference(Set<Section> sections, Set<Section> removeSections) {
        Set<Section> original = new HashSet<>(sections);
        Set<Section> remove = new HashSet<>(removeSections);
        original.removeAll(remove);
        return original;
    }

    private List<SectionEntity> convertToSectionEntities(Set<Section> sections, Long lineId) {
        return sections.stream()
                       .map(section ->
                               new SectionEntity(section.getId(),
                                       lineId,
                                       section.getUpStation().getId(),
                                       section.getDownStation().getId(),
                                       section.getDistance()))
                       .collect(Collectors.toList());
    }

    public void removeStationFromLine(Long lineId, Long stationIdToRemove) {
        checkIfExistLine(lineId);
        checkIfExistStation(stationIdToRemove);

        List<Section> findSections = sectionDao.findSectionsByLineId(lineId)
                                               .stream()
                                               .map(Section::from)
                                               .collect(Collectors.toList());
        Set<Section> sectionsSnapshot = Set.copyOf(findSections);
        Station removeStation = convertToStation(stationIdToRemove);
        Sections sections = new Sections(findSections);
        sections.removeStation(removeStation);

        updateSection(lineId, sectionsSnapshot, sections);
    }


    private void checkIfExistLine(Long lineId) {
        if (lineDao.isExistId(lineId)) {
            return;
        }
        throw new NotFoundException("해당 노선이 존재하지 않습니다.");
    }

    private void checkIfExistStation(Long stationId) {
        if (stationDao.isExistId(stationId)) {
            return;
        }
        throw new NotFoundException("해당 역이 존재하지 않습니다.");
    }

}
