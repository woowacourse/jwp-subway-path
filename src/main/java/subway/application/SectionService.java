package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findByLineId(long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        List<Station> stations = sections.getStationsInOrder();
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void addSection(final long lineId, SectionSaveRequest request) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);Station requestUpStation = stationDao.findById(request.getUpStationId());
        Station requestDownStation = stationDao.findById(request.getDownStationId());
        Section requestedSection = new Section(requestUpStation, requestDownStation, request.getDistance());

        Sections updated = sections.add(requestedSection);

        sectionDao.save(updated.getAddedCompareTo(sections), lineId);
        sectionDao.delete(updated.getRemovedCompareTo(sections));
    }

    public void removeStation(long stationId, long lineId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        Station requestedStation = stationDao.findById(stationId);

        Sections updated = sections.delete(requestedStation);

        sectionDao.save(updated.getAddedCompareTo(sections), lineId);
        sectionDao.delete(updated.getRemovedCompareTo(sections));
    }

    public List<Sections> getAllSections() {
        return sectionDao.findAll();
    }
}
