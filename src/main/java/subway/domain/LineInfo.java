package subway.domain;

import java.util.Objects;

public final class LineInfo {

	private final Long id;
	private final String name;
	private final String color;

	public LineInfo(final Long id, final String name, final String color) {
		this.id = id;
		this.name = name;
		this.color = color;
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

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final LineInfo lineInfo = (LineInfo)o;
		return Objects.equals(id, lineInfo.id) && Objects.equals(name, lineInfo.name)
			&& Objects.equals(color, lineInfo.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, color);
	}
}
