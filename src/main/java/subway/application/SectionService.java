package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Section;
import subway.dto.SectionRequest;

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
        List<Section> sections = sectionDao.findByLineIdAndStationId(lineId, stationId);

        sectionDao.deleteByStationId(lineId, stationId);
        Long upStationId = null;
        Long downStationId = null;

        if (sections.size() == 1) { // 종점인 경우
            if (sectionDao.findByLineId(lineId).isEmpty()) { // 노선에 해당 구간만 있는 경우
                return ;
            }
            Section section = sections.get(0);
            if (section.getDownStationId().equals(stationId)) {
                upStationId = section.getUpStationId();
            } else {
                downStationId = section.getDownStationId();
            }
            sectionDao.insert(new SectionRequest(downStationId, lineId, upStationId, 0));
            return ;
        }

        Section section1 = sections.get(0);
        Section section2 = sections.get(1);
        if (section1.getUpStationId().equals(stationId)) {
            upStationId = section2.getUpStationId();
            downStationId = section1.getDownStationId();
        } else {
            upStationId = section1.getUpStationId();
            downStationId = section2.getDownStationId();
        }

        int newDistance = section1.getDistance() + section2.getDistance();
        sectionDao.insert(new SectionRequest(downStationId, lineId, upStationId, newDistance));
    }
}
