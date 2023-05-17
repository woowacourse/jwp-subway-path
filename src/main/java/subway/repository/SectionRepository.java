package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public List<Section> saveUpdatedSections(final List<SectionEntity> updatedSections, final Long lineId) {
        final List<SectionEntity> originalSections = sectionDao.findSectionsByLineId(lineId);
        deleteOriginal(updatedSections, originalSections);
        final List<SectionEntity> sectionEntities = insertUpdated(updatedSections, originalSections);
        return sectionEntities.stream()
                .map(this::generateStation)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<SectionEntity> insertUpdated(final List<SectionEntity> updatedSections, final List<SectionEntity> originalSections) {
        return updatedSections.stream()
                .filter(updated -> originalSections.stream().noneMatch(original -> original.getId().equals(updated.getId())))
                .map(sectionDao::insert)
                .collect(Collectors.toUnmodifiableList());
    }

    private void deleteOriginal(final List<SectionEntity> updatedSections, final List<SectionEntity> originalSections) {
        originalSections.stream()
                .filter(original -> updatedSections.stream().noneMatch(updated -> original.getId().equals(updated.getId())))
                .forEach(sectionEntity -> sectionDao.deleteById(sectionEntity.getId()));
    }

    public List<Section> findSectionsByLineId(final Long lineId) {
        final List<SectionEntity> sectionsByLineId = sectionDao.findSectionsByLineId(lineId);
        return sectionsByLineId.stream()
                .map(this::generateStation)
                .collect(Collectors.toUnmodifiableList());
    }

    private Section generateStation(final SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                stationDao.findById(sectionEntity.getFromId()).toStation(),
                stationDao.findById(sectionEntity.getToId()).toStation(),
                sectionEntity.getDistance()
        );
    }
}
