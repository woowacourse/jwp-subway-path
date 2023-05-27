package subway.entity;

import java.util.Objects;

public class SectionEntity {

	private final Long sectionId;
	private final long lineId;
	private final long upStationId;
	private final long downStationId;
	private final long distance;

	public SectionEntity(final Long sectionId, final long lineId, final long upStationId, final long downStationId, final long distance) {
		this.sectionId = sectionId;
		this.lineId = lineId;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public long getLineId() {
		return lineId;
	}

	public long getUpStationId() {
		return upStationId;
	}

	public long getDownStationId() {
		return downStationId;
	}

	public long getDistance() {
		return distance;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof SectionEntity)) return false;
		SectionEntity that = (SectionEntity) o;
		return Objects.equals(sectionId, that.sectionId) && Objects.equals(lineId, that.lineId) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId) && Objects.equals(distance, that.distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sectionId, lineId, upStationId, downStationId, distance);
	}
}
