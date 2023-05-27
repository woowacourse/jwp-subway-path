package subway.domain.subway;

import subway.domain.common.Name;

import java.util.Objects;

public class Station {

	private final long id;
	private final Name name;

	public Station(final String name) {
		this.id = 0;
		this.name = new Name(name);
	}

	public Station(final long id, final String name) {
		this.id = id;
		this.name = new Name(name);
	}

	public void update(final String name) {
		this.name.update(name);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name.getName();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Station)) return false;
		Station station = (Station) o;
		return Objects.equals(name, station.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
