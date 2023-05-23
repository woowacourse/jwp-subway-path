package subway.domain;

import subway.entity.SectionDetailEntity;

public class LineSection {

    private final Long id;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public LineSection(final Long id, final Station previousStation, final Station nextStation, final Distance distance) {
        this.id = id;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static LineSection createByEntity(final SectionDetailEntity entity) {
        return new LineSection(
                entity.getId(),
                new Station(entity.getPreviousStationId(), entity.getPreviousStationName()),
                new Station(entity.getNextStationId(), entity.getNextStationName()),
                new Distance(entity.getDistance())
        );
    }

    public Long getId() {
        return id;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
