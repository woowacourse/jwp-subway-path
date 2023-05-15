package subway.domain;

import java.util.Map;

import subway.domain.Sections.Sections;

public interface SectionRepository {

	Sections addStationByLineId(final Long lineId, final Section newSection);

	Map<Long, Sections> findAllSections();

	Sections findSectionsByLineId();

	void deleteSectionByLineIdAndSectionId(final Long lineId, final Long sectionId);
}
