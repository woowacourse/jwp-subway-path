package subway.domain.Sections;

import java.util.Collections;
import java.util.List;

import subway.domain.Section;
import subway.error.exception.StationNotFoundException;

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
		throw StationNotFoundException.EXCEPTION;
	}
}
