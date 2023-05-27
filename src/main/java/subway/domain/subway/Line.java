package subway.domain.subway;

import subway.domain.common.Name;

import java.util.List;

public class Line {

	private final Sections sections;
	private final Name name;

	public Line(final Sections sections, final String name) {
		this.sections = sections;
		this.name = new Name(name);
	}


	public void addSection(final Section section) {
		this.sections.addSection(section);
	}

	public List<Section> getSections() {
		return sections.getSections();
	}

	public String getName() {
		return name.getName();
	}
}
