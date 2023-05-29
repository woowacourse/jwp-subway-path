package subway.domain.line;

import java.util.Objects;

public final class Station {

	private final Long id;
	private final String name;

	public Station(final Long id, final String name) {
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
		final Station station = (Station)o;
		return Objects.equals(id, station.id) && Objects.equals(name, station.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
