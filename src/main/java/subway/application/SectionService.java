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

        if (!sectionDao.findAllSectionByLineId(lineId).isEmpty()) {
            throw new DomainException(ExceptionType.LINE_HAS_STATION);
        }

        Section section = new Section(firstStationId, secondStationId, lineId, distance);
        Long id = sectionDao.insert(section);
        return SectionAddResponse.from(id);
    }

    @Transactional
    public List<SectionAddResponse> addSection(SectionAddRequest sectionAddRequest) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long targetStationId = sectionAddRequest.getTargetId();

        Sections sections = new Sections(sectionDao.findAllSectionByLineId(lineId));
        validate(stationId, sections);

        if (targetStationId == null) {
            return addAtLastStop(sectionAddRequest, sections);
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

    private List<SectionAddResponse> addAtLastStop(SectionAddRequest sectionAddRequest,
        Sections sections) {
        Long sourceStationId = sectionAddRequest.getSourceId();

        Long firstStationId = sections.findFirstStationId();
        Long lastStationId = sections.findLastStationId();

        if (!Objects.equals(sourceStationId, firstStationId) && !Objects.equals(sourceStationId, lastStationId)) {
            throw new DomainException(ExceptionType.NOT_LAST_STOP);
        }

        return getSectionAddResponses(sectionAddRequest, sections);
    }

    private List<SectionAddResponse> getSectionAddResponses(SectionAddRequest sectionAddRequest,
        Sections sections) {
        Long lineId = sectionAddRequest.getLineId();
        Long stationId = sectionAddRequest.getStationId();
        Long sourceStationId = sectionAddRequest.getSourceId();
        Integer distance = sectionAddRequest.getDistance();
        Long firstStationId = sections.findFirstStationId();
        Long lastStationId = sections.findLastStationId();

        Long newSourceStationId = stationId;
        Long newTargetStationId = firstStationId;
        if (Objects.equals(sourceStationId, lastStationId)) {
            newSourceStationId = lastStationId;
            newTargetStationId = stationId;
        }

        Long sectionId = sectionDao.insert(new Section(newSourceStationId, newTargetStationId, lineId, distance));
        return List.of(SectionAddResponse.from(sectionId));
    }
    private List<SectionAddResponse> addBetweenStations(SectionAddRequest sectionAddRequest,
        Sections sections) {
        Long sourceStationId = sectionAddRequest.getSourceId();
        Long targetStationId = sectionAddRequest.getTargetId();
        Integer distance = sectionAddRequest.getDistance();

        Section splitTargetSection = sections.findSection(sourceStationId, targetStationId);

        if (splitTargetSection.hasShorterOrSameDistanceThan(distance)) {
            throw new DomainException(ExceptionType.SECTION_CAN_NOT_BE_SPLITED);
        }

        sectionDao.deleteById(splitTargetSection.getId());
        return getSectionAddResponses(sectionAddRequest, splitTargetSection);
    }

    private List<SectionAddResponse> getSectionAddResponses(SectionAddRequest sectionAddRequest,
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

        Section firstSection = new Section(sourceStationId, stationId, lineId, firstDistance);
        Section lastSection = new Section(stationId, targetStationId, lineId, lastDistance);

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

        sectionDao.insert(new Section(sourceStationId, targetStationId, lineId, distanceSum));
    }
}
