package subway.domain.line;

import java.util.List;

public interface SectionRepository {

	Section addSection(final Long lineId, final Section newSection);

	List<Section> findSectionsByLineId(final Long id);

	void removeSection(final Section section);

}
