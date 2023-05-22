package subway.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.SectionRequest;
import subway.dto.StationsByLineResponse;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void addSection(final Long lineId, final SectionRequest sectionRequest) {
        Optional<Sections> sectionsByLineId = sectionDao.findSectionsByLineId(lineId);
        if (sectionsByLineId.isEmpty()) {  // 빈 노선에 섹션 추가
            sectionDao.insert(sectionRequest.getFromId(), sectionRequest.getToId(), sectionRequest.getDistance(),
                    lineId);
            return;
        }
        addSectionInMiddle(lineId, sectionRequest, sectionsByLineId.get());
    }

    private void addSectionInMiddle(Long lineId, SectionRequest sectionRequest, Sections sectionsByLineId) {
        List<Section> updateSections = sectionsByLineId.insert(sectionRequest.getFromId(), sectionRequest.getToId(),
                sectionRequest.getDistance());

        if (updateSections.size() == 3) {
            Section deleteSection = updateSections.get(1);
            Section addSection = updateSections.get(2);
            sectionDao.deleteSectionBySectionInfo(lineId, deleteSection);
            sectionDao.insert(addSection.getFrom().getId(), addSection.getTo().getId(), addSection.getDistanceValue(),
                    lineId);
        }

        Section addSection2 = updateSections.get(0);
        sectionDao.insert(addSection2.getFrom().getId(), addSection2.getTo().getId(), addSection2.getDistanceValue(),
                lineId);
    }

    public void deleteStationById(final Long lineId, final Long stationId) {
        Sections sectionsByStationInfo = sectionDao.findSectionsByStationInfo(lineId, stationId);
        final int sectionSize = sectionsByStationInfo.getSize();

        if (sectionSize == 0) {
            throw new IllegalArgumentException("해당 역은 노선에 존재하지 않습니다.");
        }

        if (sectionSize == 2) { // 중간에 위치한 역을 삭제할 때
            Section combineSection = sectionsByStationInfo.makeCombineSection();
            sectionDao.insert(combineSection.getFrom().getId(), combineSection.getTo().getId(),
                    combineSection.getDistanceValue(), lineId);
        }
        sectionDao.deleteSectionByStationId(lineId, stationId);
    }

    public StationsByLineResponse showStations(final Line line) {
        Sections sectionsByLineId = sectionDao.findSectionsByLineId(line.getId()).get();
        return new StationsByLineResponse(line, sectionsByLineId.showStations());
    }

}
