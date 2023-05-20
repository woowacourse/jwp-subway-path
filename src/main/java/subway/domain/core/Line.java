package subway.domain.core;

import java.util.Objects;

public class Line {
	private Long id;
	private String name;

	public Line(final String name) {
		this.name = name;
	}

	public Line(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Line line = (Line)o;
		return Objects.equals(name, line.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
