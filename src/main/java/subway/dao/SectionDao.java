package subway.dao;

import subway.domain.Section;

import java.util.List;

public interface SectionDao {

    long insert(Section section, long lineId);

    List<Section> selectAll();

    List<Section> selectSectionsByLineId(long lineId);

    long deleteById(long id);

    long deleteByLineId(long lineId);

/*    Section selectByStationIdsAndLineId(long upwardId, long downwardId, long lineId);

    Section selectEndSection(long stationId, long lineId);

    List<Section> selectSectionsByStationIdAndLineId(long stationId, long lineId);*/
}
