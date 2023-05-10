package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Direction;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionSaveRequest;

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

        Station upStation = stationDao.findById(request.getUpStationId());
        Station downStation = stationDao.findById(request.getDownStationId());

        Section requestedSection = new Section(upStation, downStation, request.getDistance());

        if (sections.isInitialSave()) {
            return sectionDao.save(requestedSection, lineId);
        }
        if (sections.isDownEndAppend(requestedSection)) {
            long savedSectionId = sectionDao.save(requestedSection, lineId);
            Section downEndSection = sections.getDownEndSection();
            sectionDao.updateNextSection(savedSectionId, downEndSection.getId());
            return savedSectionId;
        }
        if (sections.isUpEndAppend(requestedSection)) {
            Section upEndSection = sections.getUpEndSection();
            Section newSection = new Section(upStation, downStation, request.getDistance(),
                    upEndSection.getNextSectionId());
            return sectionDao.save(newSection, lineId);
        }

        Section includeSection = sections.getIncludeSection(requestedSection);
        Direction direction = includeSection.checkDirection(requestedSection);
        if (direction == Direction.INNER_LEFT) {
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
}
