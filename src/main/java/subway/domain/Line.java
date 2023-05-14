package subway.domain;

import java.util.Objects;

import subway.domain.Sections.FilledSections;
import subway.domain.Sections.Sections;

public final class Line {

	private final LineInfo lineInfo;
	private final Sections sections;

	public Line(final LineInfo lineInfo, final FilledSections sections) {
		this.lineInfo = lineInfo;
		this.sections = sections;
	}

	public LineInfo getLineInfo() {
		return lineInfo;
	}

	public Sections getSections() {
		return sections;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Line line = (Line)o;
		return Objects.equals(lineInfo, line.lineInfo) && Objects.equals(sections, line.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineInfo, sections);
	}
}
