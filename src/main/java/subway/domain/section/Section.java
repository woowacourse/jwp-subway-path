package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Station source;
    private final Station target;
    private final SectionDistance distance;

    public Section(final Station source, final Station target, final Integer distance) {
        this.source = source;
        this.target = target;
        this.distance = SectionDistance.create(distance);
    }

    public Section(final Station source, final Station target, final SectionDistance distance) {
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public static Section empty() {
        return new Section(Station.empty(), Station.empty(), SectionDistance.zero());
    }

    public boolean equalToSource(final Station newStation) {
        return source.equals(newStation);
    }

    public boolean equalToTarget(final Station newStation) {
        return target.equals(newStation);
    }

    public boolean isSameSection(final Section section) {
        return (equalToSource(section.source) && equalToTarget(section.target)) ||
            (equalToSource(section.target) && equalToTarget(section.source));
    }

    public Station source() {
        return source;
    }

    public Station target() {
        return target;
    }

    public SectionDistance distance() {
        return distance;
    }
}
