package subway.persistence.entity;

import java.util.Objects;

import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class SectionEntity {

	private Long id;
	private Long lineId;
	private Long departureId;
	private Long arrivalId;
	private Integer distance;

	public SectionEntity() {
	}

	public SectionEntity(final Long id, final Long lineId, final Long departureId, final Long arrivalId,
		final Integer distance) {
		this.id = id;
		this.lineId = lineId;
		this.departureId = departureId;
		this.arrivalId = arrivalId;
		this.distance = distance;
	}

	public static SectionEntity of(final Long lineId, final Section section) {
		final Station departure = section.getDeparture();
		final Station arrival = section.getArrival();
		final Distance distance = section.getDistance();
		return new SectionEntity(section.getId(), lineId, departure.getId(), arrival.getId(), distance.getValue());
	}

	public Long getId() {
		return id;
	}

	public Long getLineId() {
		return lineId;
	}

	public Long getDepartureId() {
		return departureId;
	}

	public Long getArrivalId() {
		return arrivalId;
	}

	public Integer getDistance() {
		return distance;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final SectionEntity that = (SectionEntity)o;
		return Objects.equals(id, that.id) && Objects.equals(lineId, that.lineId)
			&& Objects.equals(departureId, that.departureId) && Objects.equals(arrivalId,
			that.arrivalId) && Objects.equals(distance, that.distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, lineId, departureId, arrivalId, distance);
	}
}
