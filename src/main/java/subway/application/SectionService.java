package subway.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionAddResponse;
import subway.dto.SectionDeleteRequest;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionDao sectionDao;

    public SectionService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public SectionAddResponse addSection(InitialSectionAddRequest initialSectionAddRequest) {
        Long firstStationId = initialSectionAddRequest.getFirstStationId();
        Long secondStationId = initialSectionAddRequest.getSecondStationId();
        Long lineId = initialSectionAddRequest.getLineId();
        Integer distance = initialSectionAddRequest.getDistance();

        Section section = new Section(null, firstStationId, secondStationId, lineId, distance);
        Sections sections = new Sections(sectionDao.findAllSectionByLineId(lineId));
        if (!sections.hasNoSection()) {
            throw new DomainException(ExceptionType.LINE_HAS_NO_SECTION);
        }

        Long id = sectionDao.insert(section);
        return SectionAddResponse.from(id);
    }

    @Transactional
    public List<SectionAddResponse> addSection(SectionAddRequest sectionAddRequest) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long sourceStationId = sectionAddRequest.getSourceId();
        Long targetStationId = sectionAddRequest.getTargetId();
        Integer distance = sectionAddRequest.getDistance();

        Sections sections = new Sections(sectionDao.findAllSectionByLineId(lineId));

        if (sections.hasStation(stationId)) {
            throw new DomainException(ExceptionType.STATION_ALREADY_EXIST);
        }

        if (sections.hasNoSection()) {
            throw new DomainException(ExceptionType.LINE_HAS_NO_SECTION);
        }

        //종점에 추가
        if (targetStationId == null) {
            Long firstStationId = sections.findFirstStationId();
            Long lastStationId = sections.findLastStationId();

            Long sectionId;
            if (Objects.equals(sourceStationId, firstStationId)) {
                sectionId = sectionDao.insert(
                    new Section(null, lineId, firstStationId, stationId, distance));
                return List.of(SectionAddResponse.from(sectionId));
            }
            if (Objects.equals(sourceStationId, lastStationId)) {
                sectionId = sectionDao.insert(
                    new Section(null, lastStationId, stationId, lineId, distance));
                return List.of(SectionAddResponse.from(sectionId));
            }
            throw new DomainException(ExceptionType.NOT_LAST_STOP);
        }

        //사이에 추가
        Section splitTargetSection = sections.findSection(sourceStationId, targetStationId);

        if (splitTargetSection.hasShorterOrSameDistanceThan(distance)) {
            throw new DomainException(ExceptionType.SECTION_CAN_NOT_BE_SPLITED);
        }

        sectionDao.deleteById(splitTargetSection.getId());

        if (splitTargetSection.isSourceStation(sourceStationId)) {
            Section firstSection = new Section(null, sourceStationId, stationId, lineId, distance);
            Section lastSection = new Section(null, stationId, targetStationId, lineId,
                splitTargetSection.getDistance() - distance);

            Long firstSectionId = sectionDao.insert(firstSection);
            Long lastSectionId = sectionDao.insert(lastSection);
            return List.of(SectionAddResponse.from(firstSectionId), SectionAddResponse.from(lastSectionId));
        }

        Section firstSection = new Section(null, sourceStationId, stationId, lineId,
            splitTargetSection.getDistance() - distance);
        Section lastSection = new Section(null, stationId, targetStationId, lineId, distance);

        Long firstSectionId = sectionDao.insert(firstSection);
        Long lastSectionId = sectionDao.insert(lastSection);
        return List.of(SectionAddResponse.from(firstSectionId), SectionAddResponse.from(lastSectionId));
    }

    public void deleteSection(SectionDeleteRequest sectionDeleteRequest) {
        Long lineId = sectionDeleteRequest.getLineId();
        Long stationId = sectionDeleteRequest.getStationId();

        Sections sections = new Sections(sectionDao.findAllSectionByLineId(lineId));

        List<Section> sectionsIncludeStation = sections.findSectionsIncludeStation(stationId);

        if (sectionsIncludeStation.isEmpty()) {
            throw new DomainException(ExceptionType.STATION_NO_EXIST_IN_LINE);
        }

        for (Section section : sectionsIncludeStation) {
            sectionDao.deleteById(section.getId());
        }

        if (sectionsIncludeStation.size() == 1) {
            return;
        }

        Long sourceStationId = sectionsIncludeStation.stream()
            .filter(section -> section.isTargetStation(stationId))
            .map(Section::getSourceStationId)
            .findFirst()
            .orElse(null);

        Long targetStationId = sectionsIncludeStation.stream()
            .filter(section -> section.isSourceStation(stationId))
            .map(Section::getTargetStationId)
            .findFirst()
            .orElse(null);

        int distanceSum = sectionsIncludeStation.stream()
            .mapToInt(Section::getDistance)
            .sum();

        sectionDao.insert(new Section(null, sourceStationId, targetStationId, lineId, distanceSum));
    }
}
