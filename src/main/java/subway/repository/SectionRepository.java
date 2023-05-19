package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.entity.SectionEntity;
import subway.repository.converter.SectionConverter;
import subway.service.domain.Distance;
import subway.service.domain.Section;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Section save(Long lineId, Section section) {
        SectionEntity sectionEntity = SectionConverter.domainToEntity(lineId, section);
        Long sectionId = sectionDao.insert(sectionEntity);
        return new Section(
                sectionId,
                section.getPreviousStation(),
                section.getNextStation(),
                Distance.from(section.getDistance())
        );
    }

    public void deleteById(Long id) {
        int removeCount = sectionDao.deleteById(id);

        if (removeCount == 0) {
            throw new IllegalArgumentException("해당 구간이 삭제되지 않습니다.");
        }
    }

}
