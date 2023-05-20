package subway.section.presentation.dto;

import subway.section.domain.Section;
import subway.vo.Distance;

import java.util.Objects;

public class SectionSaveRequest {

    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    private SectionSaveRequest(
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final Distance distance
    ) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionSaveRequest from(final Section section) {
        return new SectionSaveRequest(
                section.getLineId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                Distance.from(section.getDistanceValue())
        );
    }

    public static SectionSaveRequest of(final Long lineId, final Long finalUpStationId, final Long finalDownStationId, final int distance) {
        return new SectionSaveRequest(
                lineId,
                finalUpStationId,
                finalDownStationId,
                Distance.from(distance)
        );
    }

    public Section toDomain() {
        return Section.of(lineId, upStationId, downStationId, distance.getValue());
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionSaveRequest that = (SectionSaveRequest) o;
        return Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId, distance);
    }
}
