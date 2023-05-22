package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.section.Section;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> saveUpdatedSections(final List<Section> updatedSections, final Long lineId) {
        final List<SectionEntity> originalSections = sectionDao.findSectionsByLineId(lineId);
        deleteOriginal(updatedSections, originalSections);
        return insertUpdated(updatedSections, originalSections, lineId);
    }

    private List<Section> insertUpdated(final List<Section> updatedSections, final List<SectionEntity> originalSections, final Long lineId) {
        return updatedSections.stream()
                .filter(updated -> originalSections.stream().noneMatch(original -> original.getId().equals(updated.getId())))
                .peek(section -> sectionDao.insert(SectionEntity.of(section, lineId)))
                .collect(Collectors.toUnmodifiableList());
    }

    private void deleteOriginal(final List<Section> updatedSections, final List<SectionEntity> originalSections) {
        originalSections.stream()
                .filter(original -> updatedSections.stream().noneMatch(updated -> original.getId().equals(updated.getId())))
                .forEach(sectionEntity -> sectionDao.deleteById(sectionEntity.getId()));
    }
}
