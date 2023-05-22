package subway.entity;

import java.util.Objects;

public class LineEntity {

	private final Long lineId;
	private String name;

	public LineEntity(final Long lineId, final String name) {
		this.lineId = lineId;
		this.name = name;
	}

	public void update(final String name) {
		this.name = name;
	}

	public Long getLineId() {
		return lineId;
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
		final LineEntity that = (LineEntity)o;
		return Objects.equals(lineId, that.lineId) && Objects.equals(
			name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lineId, name);
	}
}
