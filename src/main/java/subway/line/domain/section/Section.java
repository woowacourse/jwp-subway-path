package subway.line.domain.section;

import subway.line.domain.section.application.SectionChangeBuilder;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

public class Section {
    private final Long id;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(Long id, Station previousStation, Station nextStation, Distance distance) {
        this.id = id;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public boolean isNextStationEmpty() {
        return nextStation.equals(new EmptyStation());
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

    public SectionChangeBuilder change() {
        return new SectionChangeBuilder(id, previousStation, nextStation, distance);
    }
}
