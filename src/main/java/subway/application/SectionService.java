package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.SectionDirection;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionSaveRequest;
import subway.dto.StationResponse;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

@Service
public class SectionService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public long addSection(final long lineId, SectionSaveRequest request) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        Station requestUpStation = stationDao.findById(request.getUpStationId());
        Station requestDownStation = stationDao.findById(request.getDownStationId());
        Section requestedSection = new Section(requestUpStation, requestDownStation, request.getDistance());

        if (sections.isInitialSave()) {
            return sectionDao.save(requestedSection, lineId);
        }
        if (sections.isDownEndAppend(requestedSection)) {
            return updateDownEnd(lineId, sections, requestedSection);
        }
        if (sections.isUpEndAppend(requestedSection)) {
            return updateUpEnd(lineId, request, sections, requestUpStation, requestDownStation);
        }

        Section includeSection = sections.getIncludeSection(requestedSection);
        SectionDirection newSectionDirection = includeSection.checkNewSectionDirection(requestedSection);
        validateAddAvailable(newSectionDirection);
        if (newSectionDirection == SectionDirection.INNER_LEFT) {
            return addInnerLeft(lineId, requestedSection, includeSection);
        }
        return addInnerRight(lineId, requestedSection, includeSection);
    }

    public void removeStation(long stationId, long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        validateStationExist(stationId, sections);
        if (isLastSection(sections)) {
            sectionDao.deleteAllByLineId(lineId);
            return;
        }

        if (isUpEnd(stationId, sections)) {
            sectionDao.deleteSectionByUpStationId(stationId, lineId);
            return;
        }

        Section downEndSection = sections.getDownEndSection();
        if (sections.getDownEndSection().isSameDownStationId(stationId)) {
            updateDownEnd(stationId, lineId, sections, downEndSection);
            return;
        }
        removeMiddleStation(stationId, sections);
    }

    private static void validateAddAvailable(SectionDirection newSectionDirection) {
        if (newSectionDirection == SectionDirection.NONE) {
            throw new SectionNotFoundException();
        }
    }

    private long addInnerRight(long lineId, Section requestedSection, Section includeSection) {
        Section innerRight = new Section(requestedSection.getUpStation(), requestedSection.getDownStation(),
                requestedSection.getDistance(), includeSection.getNextSectionId());
        long savedId = sectionDao.save(innerRight, lineId);

        Section innerLeft = new Section(includeSection.getId(), includeSection.getUpStation(),
                requestedSection.getUpStation(),
                includeSection.getDistance() - requestedSection.getDistance(),
                savedId);
        sectionDao.update(innerLeft);
        return savedId;
    }

    private long addInnerLeft(long lineId, Section requestedSection, Section includeSection) {
        Section innerRight = new Section(requestedSection.getDownStation(), includeSection.getDownStation(),
                includeSection.getDistance() - requestedSection.getDistance(),
                includeSection.getNextSectionId());

        long savedId = sectionDao.save(innerRight, lineId);

        Section innerLeft = new Section(includeSection.getId(), requestedSection.getUpStation(),
                requestedSection.getDownStation(), requestedSection.getDistance(), savedId);

        sectionDao.update(innerLeft);
        return savedId;
    }

    private long updateUpEnd(long lineId, SectionSaveRequest request, Sections sections, Station requestUpStation, Station requestDownStation) {
        Section upEndSection = sections.getUpEndSection();
        Section newSection = new Section(requestUpStation, requestDownStation, request.getDistance(),
                upEndSection.getId());
        return sectionDao.save(newSection, lineId);
    }

    private long updateDownEnd(long lineId, Sections sections, Section requestedSection) {
        long savedSectionId = sectionDao.save(requestedSection, lineId);
        Section downEndSection = sections.getDownEndSection();
        sectionDao.updateSectionNext(savedSectionId, downEndSection.getId());
        return savedSectionId;
    }

    private void updateDownEnd(long stationId, long lineId, Sections sections, Section downEndSection) {
        Section section = sections.findSectionByNextSection(downEndSection);
        sectionDao.deleteSectionByDownStationId(stationId, lineId);
        sectionDao.updateSectionNext(null, section.getId());
    }

    private void removeMiddleStation(long stationId, Sections sections) {
        Section innerRight = sections.findSectionByUpStation(stationId);
        Section innerLeft = sections.findSectionByDownStation(stationId);
        Section newSection = new Section(innerLeft.getId(), innerLeft.getUpStation(), innerRight.getDownStation(),
                innerLeft.getDistance() + innerRight.getDistance(),
                innerRight.getNextSectionId());
        sectionDao.update(newSection);
        sectionDao.deleteById(innerRight.getId());
    }

    private static boolean isUpEnd(long stationId, Sections sections) {
        return sections.getUpEndSection().isSameUpStationId(stationId);
    }

    private static boolean isLastSection(Sections sections) {
        return sections.size() == 1;
    }

    private static void validateStationExist(long stationId, Sections sections) {
        if (sections.isNotExistStation(stationId)) {
            throw new StationNotFoundException();
        }
    }

    public Sections findAllByLindId(long lineId) {
         return sectionDao.findSectionsByLineId(lineId);
    }

    public List<StationResponse> findStationsInOrder(long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        System.out.println(sections);

        List<Station> stations = sections.getStationsInOrder();
        return stations.stream().map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
