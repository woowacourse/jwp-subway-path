package subway.adapter.out.persistence.dao;

import subway.adapter.out.persistence.entity.SectionEntity;

import java.util.List;

public interface SectionDao {
    void saveSection(final Long lineId, final List<SectionEntity> sectionEntities);

    List<SectionEntity> findAllByLineId(final Long lineId);

    List<SectionEntity> findAll();
}
