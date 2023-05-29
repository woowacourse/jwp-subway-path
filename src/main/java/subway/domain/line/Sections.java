package subway.domain.line;

import java.util.List;

import subway.domain.line.command.Result;
import subway.domain.line.state.EmptySections;
import subway.domain.line.state.FilledSections;
import subway.domain.line.state.SectionState;

public final class Sections {

	private final SectionState sectionState;

	public Sections(final List<Section> sections) {
		sectionState = determineState(sections);
	}

	public Result addStation(final Section newSection) {
		return sectionState.addStation(newSection);
	}

	public Result removeStation(final Station station) {
		return sectionState.removeStation(station);
	}

	private static SectionState determineState(final List<Section> sections) {
		if (sections.isEmpty()) {
			return new EmptySections();
		}
		return new FilledSections(sections);
	}

	public List<Section> getSections() {
		return sectionState.getSections();
	}
}
