package subway.domain;

import java.util.List;
import java.util.Map;

public interface SectionRepository {

	Section addSection(final Long lineId, final Section newSection);

	Map<Long, List<Section>> findAllSections();

	List<Section> findSectionsByLineId(final Long id);

	List<Section> findSectionsByLineIdAndStationId(final Long lineId, final Long StationId);

	void removeSection(final Section section);

}
