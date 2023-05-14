package subway.domain.Sections;

import java.util.List;

import subway.domain.Section;

public final class TerminalSections extends FilledSections implements StationAddable {

	TerminalSections(final List<Section> sections) {
		super(sections);
	}

	@Override
	public List<Section> addStation(final Section newSection) {
		validateCrossConnected(newSection);

		return List.of(newSection);
	}

	private void validateCrossConnected(final Section newSection) {
		if (newSection.isCrossConnected(upLineTerminal(), downLineTerminal())) {
			throw new IllegalArgumentException("순환 노선입니다.");
		}
	}

	private Section upLineTerminal() {
		return sections.get(0);
	}

	private Section downLineTerminal() {
		return sections.get(sections.size() - 1);
	}
}
