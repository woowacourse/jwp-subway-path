package subway.domain.command;

import subway.domain.Section;

public interface SectionOperation {
	boolean isInsert();

	Section getSection();
}
