package subway.dao;

import subway.Entity.SectionEntity;

import java.util.List;

public interface SectionDao {

    long insert(SectionEntity sectionEntity);

    List<SectionEntity> selectAll();

    long deleteById(long id);

    SectionEntity selectByStationIdsAndLineId(long upwardId, long downwardId, long lineId);

    SectionEntity selectSectionAtLast(long stationId, long lineId);

    List<SectionEntity> selectSectionsByStationIdAndLineId(long stationId, long lineId);
}
