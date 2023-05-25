package subway.domain;

import java.util.List;

import subway.domain.command.Result;
import subway.domain.state.EmptySections;
import subway.domain.state.FilledSections;
import subway.domain.state.SectionState;

public final class Sections {

	private final SectionState sectionState;

	public Sections(final List<Section> sections) {
		sectionState = determineState(sections);
	}

	public Result addStation(final Section newSection) {
		return sectionState.addStation(newSection);
	}

	public Result removeStation() {
		return sectionState.removeStation();
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
