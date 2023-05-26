package subway.domain.line.strategy;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.line.Section;
import subway.domain.line.Station;

public enum StrategyFactory {
	INSTANCE;

	public SectionStrategy determineStrategyForAdd(final List<Section> sections, final Section newSection) {
		if (isTerminalConnected(sections, newSection)) {
			return new TerminalStrategy(sections);
		}
		return new MiddleStrategy(sections);
	}

	public SectionStrategy determineStrategyForRemove(final List<Section> sections, final Station station) {
		final List<Section> removeSections = findRemoveSections(sections, station);

		if (isTerminalStation(removeSections)) {
			return new TerminalStrategy(removeSections);
		}
		return new MiddleStrategy(removeSections);
	}

	private List<Section> findRemoveSections(final List<Section> sections, final Station station) {
		return sections.stream()
			.filter(section -> section.containStation(station))
			.collect(Collectors.toList());
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
