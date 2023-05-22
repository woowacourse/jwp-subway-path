package subway.domain;

import subway.entity.SectionDetailEntity;

public class Section {

    private final Line line;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final Line line, final Station previousStation,
                   final Station nextStation, final Distance distance) {
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static Section createByDetailEntity(final SectionDetailEntity detailEntity) {
        return new Section(
                new Line(detailEntity.getLineId(), detailEntity.getLineName(), detailEntity.getLineColor()),
                new Station(detailEntity.getPreviousStationId(), detailEntity.getPreviousStationName()),
                new Station(detailEntity.getNextStationId(), detailEntity.getNextStationName()),
                new Distance(detailEntity.getDistance())
        );
    }

    public Line getLine() {
        return line;
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
