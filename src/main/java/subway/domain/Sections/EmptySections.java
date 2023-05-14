package subway.domain.Sections;

import java.util.Collections;
import java.util.List;

import subway.domain.Section;

public final class EmptySections extends Sections implements StationAddable {

	EmptySections() {
		super(Collections.emptyList());
	}

	@Override
	public List<Section> addStation(final Section newSection) {
		return List.of(newSection);
	}

}
