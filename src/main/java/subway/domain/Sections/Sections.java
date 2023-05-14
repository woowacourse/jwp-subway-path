package subway.domain.Sections;

import java.util.List;

import subway.domain.Section;

public abstract class Sections {

	protected final List<Section> sections;

	protected Sections(final List<Section> sections) {
		this.sections = sections;
	}

	public List<Section> getSections() {
		return sections;
	}
}
