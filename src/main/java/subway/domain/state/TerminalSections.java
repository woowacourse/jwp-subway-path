package subway.domain.state;

import java.util.List;

import subway.domain.Section;
import subway.domain.command.SectionInsertCommand;
import subway.domain.command.SectionOperation;
import subway.domain.command.SectionRemoveCommand;
import subway.error.exception.SectionConnectionException;

public final class TerminalSections implements SectionState {

	@Override
	public List<SectionOperation> addStation(final List<Section> sections, final Section newSection) {
		validateCrossConnected(sections, newSection);
		return List.of(new SectionInsertCommand(newSection));
	}

	@Override
	public List<SectionOperation> removeStation(final List<Section> sections) {
		return List.of(new SectionRemoveCommand(sections.get(0)));
	}

	private void validateCrossConnected(final List<Section> sections, final Section newSection) {
		final Section upLineTerminal = sections.get(0);
		final Section downLineTerminal = sections.get(sections.size() - 1);

		if (newSection.isCrossConnected(upLineTerminal, downLineTerminal)) {
			throw new SectionConnectionException("순환 노선입니다.");
		}
	}
}
