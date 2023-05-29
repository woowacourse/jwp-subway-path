package subway.persistence.dao;

import subway.Entity.SectionEntity;

import java.util.List;

public interface SectionDao {

    long insert(SectionEntity sectionEntity);

    List<SectionEntity> selectSectionsByLineId(long lineId);

    List<SectionEntity> selectAllSections();

    long deleteAllByLineId(Long lineId);

    void insertAll(List<SectionEntity> entities);
}
