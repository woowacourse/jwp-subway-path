package subway.domain.line;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Line {

	private final Long id;
	private final String name;
	private final String color;
	private final Sections sections;

	public Line(final String name, final String color) {
		this(null, name, color);
	}

	public Line(final Long id, final String name, final String color) {
		this(id, name, color, new Sections(Collections.emptyList()));
	}

	public Line(final Long id, final String name, final String color, final Sections sections) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.sections = sections;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public List<Section> getSections() {
		return sections.getSections();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Line line = (Line)o;
		return Objects.equals(id, line.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, color);
	}
}
