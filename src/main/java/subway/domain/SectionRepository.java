package subway.domain;

import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Long save(Long lineId, Section section) {
        return sectionDao.insert(new SectionEntity(
                lineId,
                section.getStartStationId(),
                section.getEndStationId(),
                section.getDistance()
        ));
    }

    public void delete(Long id) {
        sectionDao.deleteById(id);
    }

    public Sections findSectionsByLineId(Long lineId) {
        return new Sections(sectionDao.findAllSectionsByLineId(lineId)
                .stream()
                .map(SectionDto::toDomain)
                .collect(Collectors.toList()));
    }
}
