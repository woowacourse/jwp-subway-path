package subway.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionAddResponse;
import subway.dto.SectionDeleteRequest;
import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;

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

        if (!sectionDao.findAllSectionByLineId(lineId).isEmpty()) {
            throw new DomainException(ExceptionType.LINE_HAS_STATION);
        }

        Section section = new Section(null, firstStationId, secondStationId, lineId, distance);
        Long id = sectionDao.insert(section);
        return SectionAddResponse.from(id);
    }

    @Transactional
    public List<SectionAddResponse> addSection(SectionAddRequest sectionAddRequest) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long sourceStationId = sectionAddRequest.getSourceId();
        Long targetStationId = sectionAddRequest.getTargetId();

        Sections sections = new Sections(sectionDao.findAllSectionByLineId(lineId));
        validate(stationId, sections);

        if (sourceStationId == null) {
            return addAtUpLineLastStop(sectionAddRequest, sections);
        }

        if (targetStationId == null) {
            return addAtDownLineLastStop(sectionAddRequest, sections);
        }

        return addBetweenStations(sectionAddRequest, sections);
    }

    private void validate(Long stationId, Sections sections) {
        if (sections.hasStation(stationId)) {
            throw new DomainException(ExceptionType.STATION_ALREADY_EXIST);
        }

        if (sections.hasNoSection()) {
            throw new DomainException(ExceptionType.LINE_HAS_NO_SECTION);
        }
    }

    private List<SectionAddResponse> addAtUpLineLastStop(SectionAddRequest sectionAddRequest, Sections sections) {
        Long firstStationId = sections.findFirstStationId();

        if (!Objects.equals(sectionAddRequest.getTargetId(), firstStationId)) {
            throw new DomainException(ExceptionType.NOT_UP_LINE_LAST_STOP);
        }

        Long addedSectionId = addLastStopSection(sectionAddRequest, sections);
        return List.of(SectionAddResponse.from(addedSectionId));
    }

    private List<SectionAddResponse> addAtDownLineLastStop(SectionAddRequest sectionAddRequest, Sections sections) {
        Long lastStationId = sections.findLastStationId();

        if (!Objects.equals(sectionAddRequest.getSourceId(), lastStationId)) {
            throw new DomainException(ExceptionType.NOT_DOWN_LINE_LAST_STOP);
        }

        Long addedSectionId = addLastStopSection(sectionAddRequest, sections);
        return List.of(SectionAddResponse.from(addedSectionId));
    }

    private Long addLastStopSection(SectionAddRequest sectionAddRequest,
        Sections sections) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Integer distance = sectionAddRequest.getDistance();
        Long firstStationId = sections.findFirstStationId();
        Long lastStationId = sections.findLastStationId();

        Long newSourceStationId = stationId;
        Long newTargetStationId = firstStationId;
        if (firstStationId == null) {
            newSourceStationId = lastStationId;
            newTargetStationId = stationId;
        }

        return sectionDao.insert(new Section(null, newSourceStationId, newTargetStationId, lineId, distance));
    }

    private List<SectionAddResponse> addBetweenStations(SectionAddRequest sectionAddRequest,
        Sections sections) {
        Long sourceStationId = sectionAddRequest.getSourceId();
        Long targetStationId = sectionAddRequest.getTargetId();
        Integer distance = sectionAddRequest.getDistance();

        Section splitTargetSection = sections.findSection(sourceStationId, targetStationId);

        if (splitTargetSection.hasShorterOrSameDistanceThan(distance)) {
            throw new DomainException(ExceptionType.SECTION_CAN_NOT_BE_SPLIT);
        }

        sectionDao.deleteById(splitTargetSection.getId());
        return addNewSections(sectionAddRequest, splitTargetSection);
    }

    private List<SectionAddResponse> addNewSections(SectionAddRequest sectionAddRequest,
        Section splitTargetSection) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long sourceStationId = sectionAddRequest.getSourceId();
        Long targetStationId = sectionAddRequest.getTargetId();
        Integer firstDistance = sectionAddRequest.getDistance();
        Integer lastDistance = splitTargetSection.getDistance() - firstDistance;

        if (splitTargetSection.isTargetStation(sourceStationId)) {
            firstDistance = splitTargetSection.getDistance() - firstDistance;
            lastDistance = sectionAddRequest.getDistance();
        }

        Section firstSection = new Section(null, sourceStationId, stationId, lineId, firstDistance);
        Section lastSection = new Section(null, stationId, targetStationId, lineId, lastDistance);

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

        connectRemainedStations(lineId, stationId, sectionsIncludeStation);
    }

    private void connectRemainedStations(Long lineId, Long stationId, List<Section> sectionsIncludeStation) {
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
