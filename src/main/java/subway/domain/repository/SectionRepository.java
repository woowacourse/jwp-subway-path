package subway.domain.repository;

import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;
import subway.domain.Sections;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public long save(long lineId, Section section) {
        return sectionDao.insert(new SectionEntity(
                lineId,
                section.getStartStationId(),
                section.getEndStationId(),
                section.getDistance().value()
        ));
    }

    public void delete(Section section) {
        sectionDao.deleteById(section.getId());
    }

    public Sections findSectionsByLineId(long lineId) {
        return new Sections(sectionDao.findAllSectionsWithStationNameByLineId(lineId)
                .stream()
                .map(SectionDto::toDomain)
                .collect(Collectors.toList()));
    }

    public Sections findAll() {
        return new Sections(sectionDao.findAllSectionsWithStationName()
                .stream()
                .map(SectionDto::toDomain)
                .collect(Collectors.toList()));
    }
}
