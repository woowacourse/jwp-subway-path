package subway.domain.Sections;

import java.util.List;

import subway.domain.Section;

public final class SectionsFactory {

	public static Sections createForFind(List<Section> sections) {
		if (sections.isEmpty()) {
			return new EmptySections();
		}
		return new FilledSections(sections);
	}

	public static StationAddable createForAdd(List<Section> sections, Section newSection) {
		if (sections.isEmpty()) {
			return new EmptySections();
		}

		if (isTerminalConnected(newSection, sections)) {
			return new TerminalSections(sections);
		}

		return new MiddleSections(sections);
	}

	public static StationRemovable createForRemove(List<Section> sections) {
		if (sections.isEmpty()) {
			return new EmptySections();
		}

		if (isTerminalStation(sections)) {
			return new TerminalSections(sections);
		}

		return new MiddleSections(sections);
	}

	private static boolean isTerminalConnected(final Section newSection, final List<Section> sortedSections) {
		final Section upLineTerminal = sortedSections.get(0);
		final Section downLineTerminal = sortedSections.get(sortedSections.size() - 1);

		return newSection.isTerminalConnected(upLineTerminal, downLineTerminal);
	}

	private static boolean isTerminalStation(final List<Section> sections) {
		return sections.size() == 1;
	}
}
