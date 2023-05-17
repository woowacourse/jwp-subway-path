package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.entity.SectionEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Section save(Long lineId, Section section) {
        SectionEntity sectionEntity = SectionEntity.of(lineId, section);
        sectionDao.save(sectionEntity);
        return new Section(section.getLeft(), section.getRight(), new Distance(section.getDistance()));
    }

    public void delete(Long leftStationId, Long rightStationId) {
        sectionDao.deleteByStationId(leftStationId, rightStationId);
    }
}
