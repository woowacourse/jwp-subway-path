package subway.domain.state;

import java.util.List;

import subway.domain.Section;
import subway.domain.command.SectionInsertCommand;
import subway.domain.command.SectionOperation;
import subway.error.exception.StationNotFoundException;

public final class EmptySections implements SectionState {
	@Override
	public List<SectionOperation> addStation(final List<Section> sections, final Section newSection) {
		final SectionOperation sectionInsertCommand = new SectionInsertCommand(newSection);
		return List.of(sectionInsertCommand);
	}

	@Override
	public List<SectionOperation> removeStation(final List<Section> sections) {
		throw StationNotFoundException.EXCEPTION;
	}
}
