package subway.domain;

import java.util.List;
import java.util.Map;

public interface SectionRepository {

	Map<Long, List<Section>> findAllSections();

	List<Section> findSectionsByLineId(final Long id);

	List<Section> findSectionsByLineIdAndStationId(final Long lineId, final Long StationId);

	Section findByStationNames(final String departure, final String arrival, final Integer distance);

	List<Section> addStation(final Long lineId, final List<Section> sections);

	void removeStation(final Long lineId, final List<Section> sections);
}
