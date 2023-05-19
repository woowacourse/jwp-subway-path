package subway.persistence.entity;

import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class SectionEntity {

    private final Long id;
    private final Long beforeStation;
    private final Long nextStation;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(final Long id, final Long beforeStation, final Long nextStation, final Integer distance, final Long lineId) {
        this.id = id;
        this.beforeStation = beforeStation;
        this.nextStation = nextStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static SectionEntity from(final Long lineId, final Section section) {
        return new SectionEntity(
                section.getId(),
                section.getBeforeStation().getId(),
                section.getNextStation().getId(),
                section.getDistance().getValue(),
                lineId
        );
    }

    public Section mapToSection() {
        return new Section(id, new Station(beforeStation), new Station(nextStation), new Distance(distance));
    }

    public Long getId() {
        return id;
    }

    public Long getBeforeStation() {
        return beforeStation;
    }

    public Long getNextStation() {
        return nextStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
