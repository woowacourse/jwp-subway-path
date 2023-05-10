package subway.section.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Section {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Section(final Long id, final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        validate(distance);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(final Long id, final Section section) {
        this(id, section.lineId, section.upStationId, section.downStationId, section.distance);
    }

    public Section(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    private void validate(final int distance) {
        if (distance > 0) {
            return;
        }
        throw new IllegalArgumentException("거리는 양의 정수만 가능합니다.");
    }
}
