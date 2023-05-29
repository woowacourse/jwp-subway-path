package subway.domain.line.command;

import subway.domain.line.Section;

public final class SectionInsertCommand extends AbstractSectionCommand {

	public SectionInsertCommand(final Section section) {
		super(section);
	}

	@Override
	public boolean isInsert() {
		return true;
	}

}
