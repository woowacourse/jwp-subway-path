package subway.dto;

import subway.domain.LineInfo;
import subway.domain.Section;

public class LineSection {

	private LineInfo lineInfo;

	private Section section;

	public LineSection(final LineInfo lineInfo, final Section section) {
		this.lineInfo = lineInfo;
		this.section = section;
	}

	public LineInfo getLineInfo() {
		return lineInfo;
	}

	public Section getSection() {
		return section;
	}
}
