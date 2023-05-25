package subway.domain.strategy;

import java.util.List;

import subway.domain.Section;

public enum StrategyFactory {
	INSTANCE;

	public SectionStrategy determineStrategyForAdd(final List<Section> sections, final Section newSection) {
		if (isTerminalConnected(sections, newSection)) {
			return new TerminalStrategy(sections);
		}
		return new MiddleStrategy(sections);
	}

	public SectionStrategy determineStrategyForRemove(final List<Section> sections) {
		if (isTerminalStation(sections)) {
			return new TerminalStrategy(sections);
		}
		return new MiddleStrategy(sections);
	}

	private boolean isTerminalConnected(final List<Section> sections, final Section newSection) {
		final Section upLineTerminal = sections.get(0);
		final Section downLineTerminal = sections.get(sections.size() - 1);
		return newSection.isTerminalConnected(upLineTerminal, downLineTerminal);
	}

	private boolean isTerminalStation(final List<Section> sections) {
		return sections.size() == 1;
	}
}
