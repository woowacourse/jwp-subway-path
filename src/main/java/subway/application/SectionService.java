package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.dto.SectionRequest;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long save(final SectionRequest sectionRequest) {
        return sectionDao.insert(new Section(
                sectionRequest.getLineId(),
                sectionRequest.getUpStationId(),
                sectionRequest.getStationId(),
                new Distance(sectionRequest.getDistance())
        ));
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.isEmpty()) {
            throw new NoSuchElementException("해당하는 값이 없습니다.");
        }

        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.size() == 1) {
            linkIfFinalStation(lineId, stationId, sectionEntities.get(0));
            return;
        }
        link(lineId, stationId, sectionEntities);
    }

    private void linkIfFinalStation(final Long lineId, final Long stationId, final SectionEntity sectionEntity) {
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return;
        }
        if (sectionEntity.getDownStationId().equals(stationId)) {
            Long upStationId = sectionEntity.getUpStationId();
            sectionDao.insert(new Section(lineId, upStationId, null, new Distance(0)));
            return;
        }
        Long downStationId = sectionEntity.getDownStationId();
        sectionDao.insert(new Section(lineId, null, downStationId, new Distance(0)));
    }

    private void link(final Long lineId, final Long stationId, final List<SectionEntity> sectionEntities) {
        SectionEntity previousSection = sectionEntities.get(0);
        SectionEntity nextSection = sectionEntities.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStationId().equals(stationId)) {
            Long upStationId = nextSection.getUpStationId();
            Long downStationId = previousSection.getDownStationId();
            sectionDao.insert(new Section(lineId, upStationId, downStationId, new Distance(newDistance)));
            return;
        }
        Long upStationId = previousSection.getUpStationId();
        Long downStationId = nextSection.getDownStationId();
        sectionDao.insert(new Section(lineId, upStationId, downStationId, new Distance(newDistance)));
    }
}
