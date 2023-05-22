package subway.domain;

import subway.entity.SectionDetailEntity;

public class Section {

    private final Long id;
    private final Line line;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final long id, final Line line, final Station previousStation,
                   final Station nextStation, final Distance distance) {
        this.id = id;
        this.line = line;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public static Section createByDetailEntity(final SectionDetailEntity detailEntity) {
        return new Section(
                detailEntity.getId(),
                new Line(detailEntity.getLineId(), detailEntity.getLineName(), detailEntity.getLineColor()),
                new Station(detailEntity.getPreviousStationId(), detailEntity.getPreviousStationName()),
                new Station(detailEntity.getNextStationId(), detailEntity.getNextStationName()),
                new Distance(detailEntity.getDistance())
        );
    }

    public Long getId() {
        return id;
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
