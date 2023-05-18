package subway.section.domain.repository;

import org.springframework.stereotype.Repository;
import subway.section.domain.Section;
import subway.section.domain.entity.SectionEntity;
import subway.section.exception.SectionNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SectionRepositoryMapper implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryMapper(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }


    @Override
    public Long insert(final Section section) {
        return sectionDao.insert(section.toEntity());
    }

    @Override
    public Section findById(final Long id) {
        return sectionDao.findById(id)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Section findByUpStationId(final Long upStationId) {
        return sectionDao.findByUpStationId(upStationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Optional<SectionEntity> findOptionalByUpStationId(final Long upStationId) {
        return sectionDao.findByUpStationId(upStationId);
    }

    @Override
    public Section findLeftSectionByStationId(final Long stationId) {
        return sectionDao.findLeftSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Section findRightSectionByStationId(final Long stationId) {
        return sectionDao.findRightSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public List<Section> findAll() {
        return sectionDao.findAll().stream()
                .map(SectionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(final Long id) {
        sectionDao.deleteById(id);
    }

}
