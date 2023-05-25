package subway.line.domain.section.application;

import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

public class SectionChangeBuilder {
    private Long id;
    private Station previousStation;
    private Station nextStation;
    private Distance distance;

    public SectionChangeBuilder(Long id, Station previousStation, Station nextStation, Distance distance) {
        this.id = id;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public SectionChangeBuilder id(Long id) {
        return new SectionChangeBuilder(id, previousStation, nextStation, distance);
    }

    public SectionChangeBuilder previousStation(Station previousStation) {
        return new SectionChangeBuilder(id, previousStation, nextStation, distance);
    }

    public SectionChangeBuilder nextStation(Station nextStation) {
        return new SectionChangeBuilder(id, previousStation, nextStation, distance);
    }

    public SectionChangeBuilder subtractDistance(Distance distance) {
        return new SectionChangeBuilder(id, previousStation, nextStation, this.distance.subtract(distance));
    }

    public SectionChangeBuilder addDistance(Distance distance) {
        return new SectionChangeBuilder(id, previousStation, nextStation, this.distance.add(distance));
    }

    public SectionChangeBuilder distance(Distance distance) {
        return new SectionChangeBuilder(id, previousStation, nextStation, distance);
    }

    public Section done() {
        return new Section(id, previousStation, nextStation, distance);
    }
}
