package subway.dao;

import subway.domain.Section;

import java.util.List;

public interface SectionDao {

    long insert(final Section section, final long lineId);

    List<Section> selectAll();

    List<Section> selectSectionsByLineId(final long lineId);

    long deleteById(final long id);

    long deleteByLineId(final long lineId);
}
