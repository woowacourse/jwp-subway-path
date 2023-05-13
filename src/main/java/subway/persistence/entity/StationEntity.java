package subway.persistence.entity;

import java.util.Objects;

public class StationEntity {
	private Long id;
	private String name;

	public StationEntity() {
	}

	public StationEntity(final String name) {
		this.name = name;
	}

	public StationEntity(final Long id, final String name) {
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StationEntity station = (StationEntity) o;
		return id.equals(station.id) && name.equals(station.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
