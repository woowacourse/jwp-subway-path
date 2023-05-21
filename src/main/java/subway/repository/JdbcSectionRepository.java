package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;

    public JdbcSectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public void insertAll(final Long lineId, final Sections sections) {
        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(it -> SectionEntity.of(lineId, it))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
    }

    @Override
    public Sections findByLineId(final Long lineId) {
        final List<Section> sections = sectionDao.findByLineId(lineId).stream()
                .map(SectionStationEntity::toDomain)
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    @Override
    public void deleteAllByLineId(final Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
    }
}
