package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dto.SectionRequest;
import subway.entity.SectionEntity;

import java.util.List;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long saveSection(final SectionRequest sectionRequest) {
        return sectionDao.insert(sectionRequest);
    }

    public void deleteSectionByStationId(final Long lineId, final Long stationId) {
        // TODO: 값이 비었을 경우 throw
        List<SectionEntity> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);

        sectionDao.deleteByStationId(lineId, stationId);
        Long upStationId = null;
        Long downStationId = null;

        if (sectionEntities.size() == 1) { // 종점인 경우
            if (sectionDao.findIdByLineId(lineId).isEmpty()) { // 노선에 해당 구간만 있는 경우
                return ;
            }
            SectionEntity sectionEntity = sectionEntities.get(0);
            if (sectionEntity.getDownStationId().equals(stationId)) {
                upStationId = sectionEntity.getUpStationId();
            } else {
                downStationId = sectionEntity.getDownStationId();
            }
            sectionDao.insert(new SectionRequest(downStationId, lineId, upStationId, 0));
            return ;
        }

        SectionEntity sectionEntity1 = sectionEntities.get(0);
        SectionEntity sectionEntity2 = sectionEntities.get(1);
        if (sectionEntity1.getUpStationId().equals(stationId)) {
            upStationId = sectionEntity2.getUpStationId();
            downStationId = sectionEntity1.getDownStationId();
        } else {
            upStationId = sectionEntity1.getUpStationId();
            downStationId = sectionEntity2.getDownStationId();
        }

        int newDistance = sectionEntity1.getDistance() + sectionEntity2.getDistance();
        sectionDao.insert(new SectionRequest(downStationId, lineId, upStationId, newDistance));
    }
}
