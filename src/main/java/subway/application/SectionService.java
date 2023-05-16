package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.SectionDirection;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

@Service
@Transactional
public class SectionService {
    public static final int LAST_REMAIN = 1;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findStationsInOrder(long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        List<Station> stations = sections.getStationsInOrder();
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
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
            return updateDownEndAppend(lineId, sections, requestedSection);
        }
        if (sections.isUpEndAppend(requestedSection)) {
            return updateUpEnd(lineId, request, sections, requestUpStation, requestDownStation);
        }

        Section includeSection = sections.getIncludeSection(requestedSection);
        SectionDirection newSectionDirection = includeSection.checkNewSectionDirection(requestedSection);
        if (newSectionDirection == SectionDirection.INNER_LEFT) {
            return addInnerLeft(lineId, requestedSection, includeSection);
        }
        return addInnerRight(lineId, requestedSection, includeSection);
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

    private long updateDownEndAppend(long lineId, Sections sections, Section requestedSection) {
        long savedSectionId = sectionDao.save(requestedSection, lineId);
        Section downEndSection = sections.getDownEndSection();
        sectionDao.updateSectionNext(savedSectionId, downEndSection.getId());
        return savedSectionId;
    }

    public void removeStation(long stationId, long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        validateStationExist(stationId, sections);

        if (sections.isLastRemained()) {
            sectionDao.deleteAllByLineId(lineId);
            return;
        }

        if (sections.isUpEnd(stationId)) {
            sectionDao.deleteSectionByUpStationId(stationId, lineId);
            return;
        }

        if (sections.isDownEnd(stationId)) {
            updateDownEndRemove(stationId, lineId, sections);
            return;
        }
        removeMiddleStation(stationId, sections);
    }

    private void validateStationExist(long stationId, Sections sections) {
        if (sections.isNotExistStation(stationId)) {
            throw new StationNotFoundException();
        }
    }

    private void updateDownEndRemove(long stationId, long lineId, Sections sections) {
        Section previousDownEnd = sections.findPreviousSection(sections.getDownEndSection());
        sectionDao.deleteSectionByDownStationId(stationId, lineId);
        sectionDao.updateSectionNext(null, previousDownEnd.getId());
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
}
