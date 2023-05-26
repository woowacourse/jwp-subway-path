package subway.domain.line.command;

import subway.domain.line.Section;

public interface SectionOperation {
	boolean isInsert();

	Section getSection();
}
