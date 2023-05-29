package subway.domain.line.command;

import subway.domain.line.Section;

public abstract class AbstractSectionCommand implements SectionOperation {

	protected final Section section;

	AbstractSectionCommand(final Section section) {
		this.section = section;
	}

	public final Section getSection() {
		return section;
	}
}
