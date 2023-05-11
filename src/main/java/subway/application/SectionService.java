package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.SectionPart;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionSaveRequest;
import subway.dto.StationResponse;
import subway.exception.StationNotExistException;

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
            long savedSectionId = sectionDao.save(requestedSection, lineId);
            Section downEndSection = sections.getDownEndSection();
            sectionDao.updateSectionNext(savedSectionId, downEndSection.getId());
            return savedSectionId;
        }
        if (sections.isUpEndAppend(requestedSection)) {
            Section upEndSection = sections.getUpEndSection();
            Section newSection = new Section(requestUpStation, requestDownStation, request.getDistance(),
                    upEndSection.getId());
            return sectionDao.save(newSection, lineId);
        }

        Section includeSection = sections.getIncludeSection(requestedSection);
        SectionPart sectionPart = includeSection.checkDirection(requestedSection);
        if (sectionPart == SectionPart.INNER_LEFT) {
            Section innerRight = new Section(requestedSection.getDownStation(), includeSection.getDownStation(),
                    includeSection.getDistance() - requestedSection.getDistance(),
                    includeSection.getNextSectionId());

            long savedId = sectionDao.save(innerRight, lineId);

            Section innerLeft = new Section(includeSection.getId(), requestedSection.getUpStation(),
                    requestedSection.getDownStation(), requestedSection.getDistance(), savedId);

            sectionDao.update(innerLeft);
            return savedId;
        }

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

    public void removeStation(long stationId, long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        if (sections.isNotExistStation(stationId)) {
            throw new StationNotExistException();
        }

        if (sections.size() == 1) {
            sectionDao.deleteByLineId(lineId);
        }
        if (sections.getUpEndSection().isSameUpStationId(stationId)) {
            sectionDao.deleteSectionByUpStationId(stationId, lineId);
            return;
        }
        Section downEndSection = sections.getDownEndSection();
        if (downEndSection.isSameDownStationId(stationId)) {
            Section section = sections.findSectionByNextSection(downEndSection);
            sectionDao.deleteSectionByDownStationId(stationId, lineId);
            sectionDao.updateSectionNext(null, section.getId());
            return;
        }
        Section innerRight = sections.findSectionByUpStation(stationId);
        Section innerLeft = sections.findSectionByDownStation(stationId);
        Section newSection = new Section(innerLeft.getId(), innerLeft.getUpStation(), innerRight.getDownStation(),
                innerLeft.getDistance() + innerRight.getDistance(),
                innerRight.getNextSectionId());
        sectionDao.update(newSection);
        sectionDao.deleteById(innerRight.getId());
    }

    public Sections findAllByLindId(long lineId) {
         return sectionDao.findSectionsByLineId(lineId);
    }

    public List<StationResponse> findStationsInOrder(long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        System.out.println(sections);

        List<Station> stations = sections.getStationsInOrder();
        return stations.stream().map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }
}
