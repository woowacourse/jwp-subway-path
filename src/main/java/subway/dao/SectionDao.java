package subway.dao;

import subway.Entity.SectionEntity;

import java.util.List;

public interface SectionDao {

    long insert(SectionEntity sectionEntity);

    List<SectionEntity> selectSectionsByLineId(long lineId);

    long deleteAllByLineId(Long lineId);

    void insertAll(List<SectionEntity> entities);
}
