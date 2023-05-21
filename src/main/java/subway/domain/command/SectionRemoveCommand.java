package subway.domain.command;

import subway.domain.Section;

public class SectionRemoveCommand extends AbstractSectionCommand {

	public SectionRemoveCommand(final Section section) {
		super(section);
	}

	@Override
	public boolean isInsert() {
		return false;
	}
}
