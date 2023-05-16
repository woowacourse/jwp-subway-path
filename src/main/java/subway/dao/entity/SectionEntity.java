package subway.dao.entity;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Map;

public class SectionEntity {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;
    private final Integer distance;

    public SectionEntity(final Long id, final Long upStationId, final Long downStationId, final Long lineId, final Integer distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public static SectionEntity of(final Section section, final Line line) {
        return new SectionEntity(
                section.getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                line.getId(),
                section.getDistance().getValue()
        );
    }

    public Section convertToSection(final Map<Long, Station> stations) {
        return new Section(
                this.id,
                stations.get(this.upStationId),
                stations.get(this.downStationId),
                new Distance(this.distance));
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
