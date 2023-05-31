package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;

@Repository
public class SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void batchSave(Line line) {
        List<SectionEntity> sectionEntities = line.getSectionsByList().stream()
                .map(section -> new SectionEntity(
                        section.getUpStation().getId(),
                        section.getDownStation().getId(),
                        line.getId(),
                        section.getDistance()))
                .collect(Collectors.toList());
        sectionDao.batchSave(sectionEntities);
    }

    public void deleteByLineId(Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }
}
