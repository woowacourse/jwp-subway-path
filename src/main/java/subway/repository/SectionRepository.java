package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void saveAll(final List<Section> sections, final Line line) {
        List<SectionEntity> sectionEntities = convertToSectionEntities(line, sections);
        sectionDao.save(sectionEntities);
    }

    public void deleteAll(final List<Section> sections, final Line line) {
        List<SectionEntity> sectionEntities = convertToSectionEntities(line, sections);
        sectionDao.delete(sectionEntities);
    }

    private List<SectionEntity> convertToSectionEntities(final Line line, final List<Section> sections) {
        return sections.stream()
                .map(section -> SectionEntity.of(section, line))
                .collect(Collectors.toList());
    }
}
