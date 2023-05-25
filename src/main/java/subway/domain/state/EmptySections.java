package subway.domain.state;

import java.util.Collections;
import java.util.List;

import subway.domain.Section;
import subway.domain.command.Result;
import subway.domain.command.SectionInsertCommand;
import subway.domain.command.SectionOperation;
import subway.error.exception.StationNotFoundException;

public final class EmptySections implements SectionState {
	@Override
	public Result addStation(final Section newSection) {
		final SectionOperation sectionInsertCommand = new SectionInsertCommand(newSection);
		return new Result(List.of(sectionInsertCommand));
	}

	@Override
	public Result removeStation() {
		throw StationNotFoundException.EXCEPTION;
	}

	@Override
	public List<Section> getSections() {
		return Collections.emptyList();
	}
}
