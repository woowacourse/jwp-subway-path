package subway.dao;

import subway.entity.SectionEntity;

import java.util.List;

public interface SectionDao {
    
    SectionEntity insert(SectionEntity sectionEntity);

    void insertAll(List<SectionEntity> sectionEntities);

    List<SectionEntity> findAll();

    SectionEntity findById(Long id);

    List<SectionEntity> findByLineId(final long lineId);

    void update(SectionEntity newSectionEntity);

    void deleteById(Long id);

    void deleteAllByLineId(Long lineId);
}
