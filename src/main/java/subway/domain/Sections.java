package subway.domain;

import java.util.List;

public final class Sections {

	private final List<Section> sections;

	Sections(final List<Section> sections) {
		this.sections = sections;
	}

	public List<Section> getSections() {
		return sections;
	}
}
