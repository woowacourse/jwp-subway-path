package subway.domain.Sections;

import java.util.List;

import subway.domain.Section;
import subway.error.exception.SectionConnectionException;

public final class TerminalSections extends FilledSections implements StationAddable, StationRemovable {

	TerminalSections(final List<Section> sections) {
		super(sections);
	}

	@Override
	public List<Section> addStation(final Section newSection) {
		validateCrossConnected(newSection);

		return List.of(newSection);
	}

	@Override
	public List<Section> removeStation() {
		final Section removeSection = sections.get(0);
		return List.of(removeSection);
	}

	private void validateCrossConnected(final Section newSection) {
		if (newSection.isCrossConnected(upLineTerminal(), downLineTerminal())) {
			throw new SectionConnectionException("순환 노선입니다.");
		}
	}

}
