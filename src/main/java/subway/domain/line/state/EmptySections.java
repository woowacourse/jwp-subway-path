package subway.domain.line.state;

import java.util.Collections;
import java.util.List;

import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.domain.line.command.Result;
import subway.domain.line.command.SectionInsertCommand;
import subway.domain.line.command.SectionOperation;
import subway.error.exception.StationNotFoundException;

public final class EmptySections implements SectionState {
	@Override
	public Result addStation(final Section newSection) {
		final SectionOperation sectionInsertCommand = new SectionInsertCommand(newSection);
		return new Result(List.of(sectionInsertCommand));
	}

	@Override
	public Result removeStation(final Station station) {
		throw StationNotFoundException.EXCEPTION;
	}

	@Override
	public List<Section> getSections() {
		return Collections.emptyList();
	}
}
