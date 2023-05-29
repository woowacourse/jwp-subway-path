package subway.domain.line.command;

import subway.domain.line.Section;

public class SectionRemoveCommand extends AbstractSectionCommand {

	public SectionRemoveCommand(final Section section) {
		super(section);
	}

	@Override
	public boolean isInsert() {
		return false;
	}
}
