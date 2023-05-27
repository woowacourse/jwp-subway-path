package subway.entity;

import java.util.Objects;

public class StationEntity {

	private final long stationId;
	private final String name;

	public StationEntity(final long stationId, final String name) {
		this.stationId = stationId;
		this.name = name;
	}

	public long getStationId() {
		return stationId;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof StationEntity)) return false;
		StationEntity that = (StationEntity) o;
		return Objects.equals(stationId, that.stationId) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stationId, name);
	}
}
