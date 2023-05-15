package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.dto.SectionCreateRequest;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long createSection(final SectionCreateRequest sectionCreateRequest) {
        SectionEntity sectionEntity = new SectionEntity(
                sectionCreateRequest.getUpStationId(),
                sectionCreateRequest.getDownStationId(),
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getDistance()
        );

        return sectionDao.save(sectionEntity);
    }
}
