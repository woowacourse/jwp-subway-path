package subway.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.SectionRequest;
import subway.dto.StationsByLineDto;

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
        addSectionInAlreadyExist(lineId, sectionRequest, sectionsByLineId.get());
    }

    private void addSectionInAlreadyExist(Long lineId, SectionRequest sectionRequest, Sections sectionsByLineId) {
        sectionsByLineId.validateAlreadyIn(sectionRequest.getFromId(), sectionRequest.getToId());
        Optional<Section> targetSection = sectionsByLineId.findTargetSection(sectionRequest.getFromId(),
                sectionRequest.getToId());
        sectionsByLineId.checkInsertableDistance(targetSection, sectionRequest.getDistance());

        if (isInsertableAtMiddleLeft(lineId, sectionRequest, sectionsByLineId, targetSection)) {
            return;
        }
        if (isInsertableAtMiddleRight(lineId, sectionRequest, sectionsByLineId, targetSection)) {
            return;
        }

        sectionDao.insert(sectionRequest.getFromId(), sectionRequest.getToId(), sectionRequest.getDistance(), lineId);
    }

    private boolean isInsertableAtMiddleLeft(Long lineId, SectionRequest sectionRequest, Sections sectionsByLineId,
                                             Optional<Section> targetSection) {
        if (sectionsByLineId.isInsertableLeft(targetSection, sectionRequest.getToId())) {
            List<Section> leftChangeSections = sectionsByLineId.getLeftChangeSections(sectionRequest.getFromId(),
                    sectionRequest.getToId(), sectionRequest.getDistance(), targetSection);
            insertInMiddle(lineId, leftChangeSections);
            return true;
        }
        return false;
    }

    private boolean isInsertableAtMiddleRight(Long lineId, SectionRequest sectionRequest, Sections sectionsByLineId,
                                              Optional<Section> targetSection) {
        if (sectionsByLineId.isInsertableRight(targetSection, sectionRequest.getFromId())) {
            List<Section> rightChangeSections = sectionsByLineId.getRightChangeSections(sectionRequest.getFromId(),
                    sectionRequest.getToId(), sectionRequest.getDistance(), targetSection);
            insertInMiddle(lineId, rightChangeSections);
            return true;
        }
        return false;
    }

    private void insertInMiddle(Long lineId, List<Section> leftChangeSections) {
        sectionDao.insert(leftChangeSections.get(0).getFrom().getId(), leftChangeSections.get(0).getTo().getId(),
                leftChangeSections.get(0).getDistanceValue(), lineId);
        sectionDao.deleteSectionBySectionInfo(lineId, leftChangeSections.get(1));
        sectionDao.insert(leftChangeSections.get(2).getFrom().getId(), leftChangeSections.get(2).getTo().getId(),
                leftChangeSections.get(2).getDistanceValue(), lineId);
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

    public StationsByLineDto showStations(final Line line) {
        Sections sectionsByLineId = sectionDao.findSectionsByLineId(line.getId()).get();
        return new StationsByLineDto(line, sectionsByLineId.showStations());
    }

}
