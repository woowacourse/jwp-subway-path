package subway.domain.command;

import subway.domain.Section;

public final class SectionInsertCommand extends AbstractSectionCommand {

	public SectionInsertCommand(final Section section) {
		super(section);
	}

	@Override
	public boolean isInsert() {
		return true;
	}

}
