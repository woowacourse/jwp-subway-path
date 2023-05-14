package subway.dao;

import subway.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionDao {

    Optional<SectionEntity> findById(Long id);

    List<SectionEntity> findAllByLineId(final long lineId);

    List<SectionEntity> findAll();

    SectionEntity insert(SectionEntity sectionEntity);

    void insertAll(List<SectionEntity> sectionEntities);

    void update(SectionEntity newSectionEntity);

    void deleteById(Long id);

    void deleteAllByLineId(Long lineId);
}
