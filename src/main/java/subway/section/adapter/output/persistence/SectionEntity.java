package subway.section.adapter.output.persistence;

import subway.section.domain.Section;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;

public class SectionEntity {
    private final Long id;
    private final String firstStation;
    private final String secondStation;
    private final Integer distance;
    private final Long lineId;

    public SectionEntity(final String firstStation, final String secondStation, final Integer distance,
                         final Long lineId) {
        this(null, firstStation, secondStation, distance, lineId);
    }

    public SectionEntity(final Long id, final String firstStation, final String secondStation, final Integer distance,
                         final Long lineId) {
        this.id = id;
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Section toSection() {
        return new Section(
                new Station(firstStation),
                new Station(secondStation),
                new StationDistance(distance)
        );
    }

    public Long getId() {
        return id;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
