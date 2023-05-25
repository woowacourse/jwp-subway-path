package subway.domain;

import subway.entity.SectionDetailEntity;

public class LineSection {

    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public LineSection(final Station previousStation, final Station nextStation, final Distance distance) {
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static LineSection from(final SectionDetailEntity entity) {
        return new LineSection(
                new Station(entity.getPreviousStationId(), entity.getPreviousStationName()),
                new Station(entity.getNextStationId(), entity.getNextStationName()),
                new Distance(entity.getDistance())
        );
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
