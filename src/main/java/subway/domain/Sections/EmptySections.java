package subway.domain.Sections;

import java.util.Collections;
import java.util.List;

import subway.domain.Section;

public final class EmptySections extends Sections implements StationAddable, StationRemovable {

	EmptySections() {
		super(Collections.emptyList());
	}

	@Override
	public List<Section> addStation(final Section newSection) {
		return List.of(newSection);
	}

	@Override
	public List<Section> removeStation() {
		throw new IllegalArgumentException("해당하는 역이 없습니다.");
	}
}
